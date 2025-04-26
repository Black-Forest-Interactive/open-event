import {Component, input, output} from '@angular/core';
import {EventInfo, ParticipateResponse} from "@open-event-workspace/core";
import {BoardCardComponent, BoardCardToolbarActions} from "../../../shared/board-card/board-card.component";
import {RegistrationTableComponent} from "../../registration/registration-table/registration-table.component";
import {TranslatePipe, TranslateService} from "@ngx-translate/core";
import {MatButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {MatDialog} from "@angular/material/dialog";
import {RegistrationParticipantAddManualDialogComponent} from "../../registration/registration-participant-add-manual-dialog/registration-participant-add-manual-dialog.component";
import {RegistrationParticipantAddAccountDialogComponent} from "../../registration/registration-participant-add-account-dialog/registration-participant-add-account-dialog.component";
import {HotToastService} from "@ngxpert/hot-toast";

@Component({
  selector: 'app-event-details-registration',
  imports: [
    BoardCardComponent,
    RegistrationTableComponent,
    BoardCardToolbarActions,
    MatButton,
    MatIcon,
    TranslatePipe
  ],
  templateUrl: './event-details-registration.component.html',
  styleUrl: './event-details-registration.component.scss'
})
export class EventDetailsRegistrationComponent {
  event = input.required<EventInfo>()
  changeResponse = output<ParticipateResponse>()

  constructor(
    private dialog: MatDialog,
    private hotToast: HotToastService,
    private translation: TranslateService
  ) {
  }

  addParticipantAccount() {
    let event = this.event()
    let registration = event.registration?.registration
    if(!registration) return
    let dialogRef = this.dialog.open(RegistrationParticipantAddAccountDialogComponent, {data: {registration: registration}})
    dialogRef.afterClosed().subscribe(response => {
      if (response) this.handleParticipateResponse(response)
    })
  }

  addParticipantManual() {
    let event = this.event()
    let registration = event.registration?.registration
    if(!registration) return
    let dialogRef = this.dialog.open(RegistrationParticipantAddManualDialogComponent, {data: {registration: registration}})
    dialogRef.afterClosed().subscribe(response => {
      if (response) this.handleParticipateResponse(response)
    })
  }


  protected handleParticipateResponse(response: ParticipateResponse) {
    switch (response.status) {
      case 'ACCEPTED':
        this.translation.get('registration.message.accepted').subscribe(msg => this.hotToast.success(msg))
        break;
      case 'WAITING_LIST_DECREASE_SIZE':
      case 'WAITING_LIST':
        this.translation.get('registration.message.waiting').subscribe(msg => this.hotToast.info(msg))
        break;
      case 'DECLINED':
        this.translation.get('registration.message.declined').subscribe(msg => this.hotToast.warning(msg))
        break;
      case 'FAILED':
        this.translation.get('registration.message.failed').subscribe(msg => this.hotToast.error(msg))
        break;
    }
    this.changeResponse.emit(response)
  }
}
