import { Component, input, Input } from '@angular/core'
import { TranslatePipe } from '@ngx-translate/core'
import { NgClass } from '@angular/common'
import { RegistrationInfo } from '../registration.api'
import { Participant } from '../../participant'
import { EventSearchEntry } from '../../search'

interface SpaceEntry {
  hasSpaceLeft: boolean
  remainingSpace: number
  maxGuestAmount: number
}

@Component({
  selector: 'lib-registration-status',
  imports: [TranslatePipe, NgClass],
  templateUrl: './registration-status.component.html',
  styleUrl: './registration-status.component.scss'
})
export class RegistrationStatusComponent {
  maxIndicatorSize = input(10)
  vertical = input(false)

  spaceAvailable: boolean = false
  space = {
    remaining: 0,
    available: 0
  }

  indicator: any[] = []

  @Input()
  set data(info: RegistrationInfo | undefined) {
    if (info) {
      const totalAmount = info.participants.filter((p) => !p.waitingList).reduce((sum: number, p: Participant) => sum + p.size, 0)
      this.space.available = info.registration.maxGuestAmount
      this.space.remaining = this.space.available - totalAmount
      this.spaceAvailable = this.space.remaining > 0
      this.updateIndicator()
    }
  }

  @Input()
  set entry(entry: EventSearchEntry) {
    this.spaceAvailable = entry.hasSpaceLeft
    this.space.remaining = entry.remainingSpace
    this.space.available = entry.maxGuestAmount
    this.updateIndicator()
  }

  @Input()
  set public(entry: SpaceEntry) {
    this.spaceAvailable = entry.hasSpaceLeft
    this.space.remaining = entry.remainingSpace
    this.space.available = entry.maxGuestAmount
    this.updateIndicator()
  }

  get takenPct(): number {
    return this.space.available > 0 ? ((this.space.available - this.space.remaining) / this.space.available) * 100 : 0
  }
  get remainingPct(): number {
    return this.space.available > 0 ? (this.space.remaining / this.space.available) * 100 : 0
  }

  private updateIndicator() {
    if (this.maxIndicatorSize() <= 0) return
    if (this.space.available >= this.maxIndicatorSize() || !this.spaceAvailable) {
      this.indicator = []
    } else {
      this.indicator = Array.from({ length: this.space.available }, (_, i) => (i < this.space.remaining ? { key: i, value: 'bg-green-500' } : { key: i, value: 'bg-orange-300' }))
    }
  }
}
