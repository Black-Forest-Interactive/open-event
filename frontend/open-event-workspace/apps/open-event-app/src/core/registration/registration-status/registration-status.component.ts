import {Component, Input} from '@angular/core';
import {EventSearchEntry, Participant, RegistrationInfo, SharedParticipant, SharedRegistration} from "@open-event-workspace/core";
import {TranslatePipe} from "@ngx-translate/core";


@Component({
  selector: 'app-registration-status',
  templateUrl: './registration-status.component.html',
  styleUrls: ['./registration-status.component.scss'],
  imports: [
    TranslatePipe
  ],
  standalone: true
})
export class RegistrationStatusComponent {
  spaceAvailable: boolean = false
  space = {
    remaining: 0,
    available: 0
  }

  @Input()
  set data(info: RegistrationInfo | undefined) {
    if (info) {
      let totalAmount = info.participants.filter(p => !p.waitingList).reduce((sum: number, p: Participant) => sum + p.size, 0)
      this.space.available = info.registration.maxGuestAmount
      this.space.remaining = this.space.available - totalAmount
      this.spaceAvailable = this.space.remaining > 0
    }
  }

  @Input()
  set shared(info: SharedRegistration) {
    if (info) {
      let totalAmount = info.participants.filter(p => !p.waitingList).reduce((sum: number, p: SharedParticipant) => sum + p.size, 0)
      this.space.available = info.maxGuestAmount
      this.space.remaining = this.space.available - totalAmount
      this.spaceAvailable = this.space.remaining > 0
    }
  }

  @Input()
  set entry(entry: EventSearchEntry) {
    this.spaceAvailable = entry.hasSpaceLeft
    this.space.remaining = entry.remainingSpace
    this.space.available = entry.maxGuestAmount
  }

}
