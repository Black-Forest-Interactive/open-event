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
      let availableAmount = info.registration.maxGuestAmount
      this.remaining = availableAmount - totalAmount
      this.available = this.remaining > 0
    }
  }

  available: boolean = false
  remaining = 0
}
