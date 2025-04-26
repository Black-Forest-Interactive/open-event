import {Component, Input} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {HotToastService} from "@ngxpert/hot-toast";
import {RegistrationParticipateDialogComponent} from "../registration-participate-dialog/registration-participate-dialog.component";
import {TranslatePipe, TranslateService} from "@ngx-translate/core";
import {RegistrationEditDialogComponent} from "../registration-edit-dialog/registration-edit-dialog.component";
import {RegistrationCancelDialogComponent} from "../registration-cancel-dialog/registration-cancel-dialog.component";
import {AuthService, LoadingBarComponent} from "@open-event-workspace/shared";
import {Participant, ParticipateRequest, ParticipateResponse, RegistrationInfo} from '@open-event-workspace/core';
import {MatButton} from "@angular/material/button";
import {NgTemplateOutlet} from "@angular/common";
import {MatIcon} from "@angular/material/icon";
import {MatCard, MatCardActions, MatCardContent, MatCardHeader} from "@angular/material/card";
import {RegistrationStatusComponent} from "../registration-status/registration-status.component";
import {MatDivider} from "@angular/material/divider";
import {AccountComponent} from "../../account/account/account.component";
import {Roles} from "../../../shared/roles";
import {RegistrationService} from "@open-event-workspace/app";

@Component({
  selector: 'app-registration-details',
  templateUrl: './registration-details.component.html',
  styleUrls: ['./registration-details.component.scss'],
  imports: [
    MatIcon,
    TranslatePipe,
    MatCard,
    MatCardHeader,
    RegistrationStatusComponent,
    MatDivider,
    MatCardContent,
    NgTemplateOutlet,
    MatButton,
    MatCardActions,
    AccountComponent,
    LoadingBarComponent
  ],
  standalone: true
})
export class RegistrationDetailsComponent {
  registration: RegistrationInfo | undefined
  accepted: Participant[] = []
  waitList: Participant[] = []
  reloading: boolean = false
  userParticipant: Participant | undefined
  adminOrManager: boolean = false

  constructor(
    private service: RegistrationService,
    private dialog: MatDialog,
    private hotToast: HotToastService,
    private translation: TranslateService,
    private authService: AuthService
  ) {
  }

  @Input()
  set data(value: RegistrationInfo) {
    this.registration = value
    this.updateData()
  }

  ngOnInit() {
    this.adminOrManager = this.authService.hasRole(Roles.REGISTRATION_MANAGE, Roles.REGISTRATION_ADMIN)
    let principal = this.authService.getPrincipal()
    if (principal) this.adminOrManager = principal.roles.find(r => r === Roles.REGISTRATION_MANAGE || r === Roles.REGISTRATION_ADMIN) != null
  }

  participateSelf() {
    if (!this.registration) return
    if (this.reloading) return
    let dialogRef = this.dialog.open(RegistrationParticipateDialogComponent)
    dialogRef.afterClosed().subscribe(request => {
      if (request) this.requestParticipateSelf(request)
    })
  }

  editSelf() {
    if (!this.registration) return
    if (this.reloading) return

    let dialogRef = this.dialog.open(RegistrationEditDialogComponent, {data: this.userParticipant})
    dialogRef.afterClosed().subscribe(request => {
      if (request) this.requestEditSelf(request)
    })
  }

  cancelSelf() {
    if (!this.registration) return
    if (this.reloading) return

    let dialogRef = this.dialog.open(RegistrationCancelDialogComponent)
    dialogRef.afterClosed().subscribe(request => {
      if (request) this.requestCancelSelf()
    })
  }


  private updateData() {
    if (this.registration) {
      this.accepted = this.registration.participants.filter(p => !p.waitingList)
      this.waitList = this.registration.participants.filter(p => p.waitingList)
      this.userParticipant = this.registration.participants.find(p => p.author.email === this.authService.getPrincipal()?.email)
    }
  }

  private requestParticipateSelf(request: ParticipateRequest) {
    if (!this.registration) return
    if (this.reloading) return
    this.reloading = true
    this.service.addParticipant(this.registration.registration.id, request).subscribe(r => this.handleParticipateResponse(r))
  }

  private requestEditSelf(request: ParticipateRequest) {
    if (!this.registration) return
    if (this.reloading) return
    this.reloading = true
    this.service.changeParticipant(this.registration.registration.id, request).subscribe(r => this.handleParticipateResponse(r))
  }

  private requestCancelSelf() {
    if (!this.registration) return
    if (this.reloading) return
    this.reloading = true
    this.service.removeParticipant(this.registration.registration.id).subscribe(r => this.handleParticipateResponse(r))
  }


  private handleParticipateResponse(response: ParticipateResponse) {
    this.updateParticipants(response.participants)
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


  private updateParticipants(participants: Participant[]) {
    if (!this.registration) return
    this.registration.participants = participants
    this.updateData()
  }
}
