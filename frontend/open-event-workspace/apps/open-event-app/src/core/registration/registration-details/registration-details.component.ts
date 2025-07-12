import {Component, computed, model} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {HotToastService} from "@ngxpert/hot-toast";
import {RegistrationParticipateDialogComponent} from "../registration-participate-dialog/registration-participate-dialog.component";
import {TranslatePipe, TranslateService} from "@ngx-translate/core";
import {RegistrationEditDialogComponent} from "../registration-edit-dialog/registration-edit-dialog.component";
import {RegistrationCancelDialogComponent} from "../registration-cancel-dialog/registration-cancel-dialog.component";
import {AuthService, LoadingBarComponent} from "@open-event-workspace/shared";
import {ParticipateRequest, ParticipateResponse, RegistrationInfo, RegistrationStatusComponent} from '@open-event-workspace/core';
import {MatButton} from "@angular/material/button";
import {NgTemplateOutlet} from "@angular/common";
import {MatIcon} from "@angular/material/icon";
import {MatCard} from "@angular/material/card";
import {MatDivider} from "@angular/material/divider";
import {AccountComponent} from "../../account/account/account.component";
import {RegistrationService} from "@open-event-workspace/app";

@Component({
  selector: 'app-registration-details',
  templateUrl: './registration-details.component.html',
  styleUrls: ['./registration-details.component.scss'],
  imports: [
    MatIcon,
    TranslatePipe,
    MatCard,
    RegistrationStatusComponent,
    MatDivider,
    NgTemplateOutlet,
    MatButton,
    AccountComponent,
    LoadingBarComponent,
    RegistrationStatusComponent
  ],
  standalone: true
})
export class RegistrationDetailsComponent {

  data = model.required<RegistrationInfo>()
  participants = computed(() => this.data().participants)

  accepted = computed(() => this.participants().filter(p => !p.waitingList))
  waitList = computed(() => this.participants().filter(p => p.waitingList))
  reloading: boolean = false
  userParticipant = computed(() => this.participants().find(p => p.author.email === this.authService.getPrincipal()?.email))


  constructor(
    private service: RegistrationService,
    private dialog: MatDialog,
    private hotToast: HotToastService,
    private translation: TranslateService,
    private authService: AuthService
  ) {
  }

  participateSelf() {
    if (this.reloading) return
    let dialogRef = this.dialog.open(RegistrationParticipateDialogComponent)
    dialogRef.afterClosed().subscribe(request => {
      if (request) this.requestParticipateSelf(request)
    })
  }

  editSelf() {
    if (this.reloading) return

    let dialogRef = this.dialog.open(RegistrationEditDialogComponent, {data: this.userParticipant})
    dialogRef.afterClosed().subscribe(request => {
      if (request) this.requestEditSelf(request)
    })
  }

  cancelSelf() {
    if (this.reloading) return

    let dialogRef = this.dialog.open(RegistrationCancelDialogComponent)
    dialogRef.afterClosed().subscribe(request => {
      if (request) this.requestCancelSelf()
    })
  }


  private requestParticipateSelf(request: ParticipateRequest) {
    if (this.reloading) return
    this.reloading = true
    this.service.addParticipant(this.data().registration.id, request).subscribe(r => this.handleParticipateResponse(r))
  }

  private requestEditSelf(request: ParticipateRequest) {
    if (this.reloading) return
    this.reloading = true
    this.service.changeParticipant(this.data().registration.id, request).subscribe(r => this.handleParticipateResponse(r))
  }

  private requestCancelSelf() {
    if (this.reloading) return
    this.reloading = true
    this.service.removeParticipant(this.data().registration.id).subscribe(r => this.handleParticipateResponse(r))
  }


  private handleParticipateResponse(response: ParticipateResponse) {
    this.data.set({...this.data(), participants: response.participants})
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
    this.reloading = false
  }


}
