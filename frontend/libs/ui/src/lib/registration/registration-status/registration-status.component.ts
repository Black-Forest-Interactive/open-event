import { Component, computed, input, ChangeDetectionStrategy } from '@angular/core'
import { TranslatePipe } from '@ngx-translate/core'
import { EventSearchEntry, Participant, RegistrationInfo } from '@open-event/core'

interface SpaceEntry {
  hasSpaceLeft: boolean
  remainingSpace: number
  maxGuestAmount: number
}

@Component({
  selector: 'lib-registration-status',
  imports: [TranslatePipe],
  templateUrl: './registration-status.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './registration-status.component.scss'
})
export class RegistrationStatusComponent {
  compact = input(false)
  data = input<RegistrationInfo>()
  entry = input<EventSearchEntry>()
  public = input<SpaceEntry>()

  private space = computed(() => {
    const info = this.data()
    if (info) {
      const taken = info.participants.filter((p) => !p.waitingList).reduce((sum: number, p: Participant) => sum + p.size, 0)
      const available = info.registration.maxGuestAmount
      return { available, taken, remaining: available - taken }
    }
    const spaceEntry = this.entry() ?? this.public()
    if (spaceEntry) {
      const available = spaceEntry.maxGuestAmount
      const remaining = spaceEntry.remainingSpace
      return { available, remaining, taken: available - remaining }
    }
    return { available: 0, taken: 0, remaining: 0 }
  })

  readonly available = computed(() => this.space().available)
  readonly taken = computed(() => this.space().taken)
  readonly remaining = computed(() => this.space().remaining)

  readonly level = computed<'ok' | 'mid' | 'low' | 'full'>(() => {
    if (this.available() <= 0) return 'ok'
    if (this.remaining() <= 0) return 'full'
    const ratio = this.taken() / this.available()
    if (ratio >= 0.85) return 'low'
    if (ratio >= 0.6) return 'mid'
    return 'ok'
  })

  readonly fillPct = computed(() => {
    if (this.available() <= 0) return 0
    if (this.level() === 'full') return 100
    const ratio = Math.round((this.taken() / this.available()) * 100)
    return this.level() === 'ok' ? Math.max(6, ratio) : ratio
  })
}
