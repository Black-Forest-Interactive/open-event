import {Component, Input} from '@angular/core';
import {ParticipateRequest, ParticipateResponse, Registration} from "../model/registration-api";
import {RegistrationService} from "../model/registration.service";
import {Participant} from "../../participant/model/participant-api";
import {MatDialog} from "@angular/material/dialog";
import {HotToastService} from "@ngneat/hot-toast";
import {
  RegistrationParticipateDialogComponent
} from "../registration-participate-dialog/registration-participate-dialog.component";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-registration-details',
  templateUrl: './registration-details.component.html',
  styleUrls: ['./registration-details.component.scss']
})
export class RegistrationDetailsComponent {
  @Input()
  set data(value: Registration) {
    this.registration = value
    this.loadData()
  }

  registration: Registration | undefined
  participants: Participant[] = []
  accepted: Participant[] = []
  waitList: Participant[] = []
  reloading: boolean = false

  constructor(
    private service: RegistrationService, private dialog: MatDialog, private hotToast: HotToastService, private translation: TranslateService
  ) {
  }

  private loadData() {
    if (!this.registration) return
    if (this.reloading) return
    this.reloading = true
    this.service.getParticipants(this.registration.id).subscribe(d => this.handleData(d))
  }

  private handleData(d: Participant[]) {
    this.participants = d
    this.accepted = d.filter(p => !p.waitingList)
    this.waitList = d.filter(p => p.waitingList)
    this.reloading = false
  }

  participate() {
    if (!this.registration) return
    if (this.reloading) return
    let dialogRef = this.dialog.open(RegistrationParticipateDialogComponent)
    dialogRef.afterClosed().subscribe(request => {
      if (request) this.requestParticipate(request)
    })
  }

  private requestParticipate(request: ParticipateRequest) {
    if (!this.registration) return
    if (this.reloading) return
    this.reloading = true
    this.service.participate(this.registration.id, request).subscribe(r => this.handleParticipateResponse(r))
  }

  private handleParticipateResponse(response: ParticipateResponse) {
    this.handleData(response.participants)
    switch (response.status) {
      case 'ACCEPTED':
        this.translation.get('registration.participate.accepted').subscribe(msg => this.hotToast.success(msg))
        break;
      case 'WAITING_LIST_DECREASE_SIZE':
      case 'WAITING_LIST':
        this.translation.get('registration.participate.waiting').subscribe(msg => this.hotToast.info(msg))
        break;
      case 'DECLINED':
        this.translation.get('registration.participate.declined').subscribe(msg => this.hotToast.warning(msg))
        break;
      case 'FAILED':
        this.translation.get('registration.participate.failed').subscribe(msg => this.hotToast.error(msg))
        break;
    }
    this.reloading = false
  }

  showEditDialog(part: Participant) {

  }

  removeParticipant(part: Participant) {

  }
}
