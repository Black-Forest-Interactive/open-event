import { Component, computed, input, output, ChangeDetectionStrategy } from '@angular/core'
import { EventInfo, Participant } from '@open-event/core'
import { RegistrationStatusComponent } from '@open-event/ui'
import { TranslatePipe } from '@ngx-translate/core'
import { MatButton } from '@angular/material/button'
import { MatIcon } from '@angular/material/icon'
import { RouterLink } from '@angular/router'

@Component({
  selector: 'portal-event-bookbar',
  templateUrl: './event-bookbar.component.html',
  styleUrl: './event-bookbar.component.scss',
  imports: [RegistrationStatusComponent, TranslatePipe, MatButton, MatIcon, RouterLink],
  changeDetection: ChangeDetectionStrategy.Eager
})
export class EventBookbarComponent {
  info = input.required<EventInfo>()
  userParticipant = input<Participant | undefined>(undefined)
  reloading = input(false)

  participate = output<void>()
  edit = output<void>()
  cancelParticipation = output<void>()

  readonly event = computed(() => this.info().event)
  readonly canEdit = computed(() => this.info().canEdit)
  readonly readOnly = computed(() => this.event().status === 'ENDED' || this.event().status === 'CANCELED')
  readonly registration = computed(() => this.info().registration)
  readonly taken = computed(() => this.registration()?.participants.filter((p) => !p.waitingList).reduce((sum, p) => sum + p.size, 0) ?? 0)
  readonly capacity = computed(() => this.registration()?.registration.maxGuestAmount ?? 0)
  readonly isFull = computed(() => this.taken() >= this.capacity())
  readonly waitlistPosition = computed(() => {
    const participant = this.userParticipant()
    if (!participant?.waitingList) return 0
    const waitlist = (this.registration()?.participants ?? []).filter((p) => p.waitingList).sort((a, b) => a.rank - b.rank)
    return waitlist.findIndex((p) => p.id === participant.id) + 1
  })
}
