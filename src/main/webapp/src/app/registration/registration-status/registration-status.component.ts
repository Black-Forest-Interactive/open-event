import {Component, Input} from '@angular/core';
import {RegistrationInfo} from "../model/registration-api";
import {Participant} from "../../participant/model/participant-api";

@Component({
  selector: 'app-registration-status',
  templateUrl: './registration-status.component.html',
  styleUrls: ['./registration-status.component.scss']
})
export class RegistrationStatusComponent {
  @Input()
  set data(info: RegistrationInfo | undefined) {
    if (info) {
      let totalAmount = info.participants.filter(p => !p.waitingList).reduce((sum: number, p: Participant) => sum + p.size, 0)
      this.space.available = info.registration.maxGuestAmount
      this.space.remaining = this.space.available - totalAmount
      this.spaceAvailable = this.space.remaining > 0
    }
  }

  spaceAvailable: boolean = false
  space = {
    remaining: 0,
    available: 0
  }

}
