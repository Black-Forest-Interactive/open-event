import {Component, input, Input} from '@angular/core';
import {EventSearchEntry, Participant, RegistrationInfo, SharedParticipant, SharedRegistration} from "@open-event-workspace/core";
import {TranslatePipe} from "@ngx-translate/core";
import {NgClass} from "@angular/common";

@Component({
  selector: 'lib-registration-status',
  imports: [TranslatePipe, NgClass],
  templateUrl: './registration-status.component.html',
  styleUrl: './registration-status.component.scss'
})
export class RegistrationStatusComponent {

  maxIndicatorSize = input(10)

  spaceAvailable: boolean = false
  space = {
    remaining: 0,
    available: 0
  }

  indicator: string[] = []

  @Input()
  set data(info: RegistrationInfo | undefined) {
    if (info) {
      let totalAmount = info.participants.filter(p => !p.waitingList).reduce((sum: number, p: Participant) => sum + p.size, 0)
      this.space.available = info.registration.maxGuestAmount
      this.space.remaining = this.space.available - totalAmount
      this.spaceAvailable = this.space.remaining > 0
      this.updateIndicator()
    }
  }

  @Input()
  set shared(info: SharedRegistration) {
    if (info) {
      let totalAmount = info.participants.filter(p => !p.waitingList).reduce((sum: number, p: SharedParticipant) => sum + p.size, 0)
      this.space.available = info.maxGuestAmount
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

  private updateIndicator() {
    if (this.maxIndicatorSize() <= 0) return
    if (this.space.available >= this.maxIndicatorSize() || !this.spaceAvailable) {
      this.indicator = []
    } else {
      this.indicator = Array.from({length: this.space.available}, (_, i) => (i < this.space.remaining ? 'bg-green-500' : 'bg-orange-300'));
    }
  }

}
