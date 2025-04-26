import {Component, Input, ViewChild} from '@angular/core';
import {MatSort, MatSortHeader} from "@angular/material/sort";
import {MatTableDataSource, MatTableModule} from "@angular/material/table";
import {RegistrationEditDialogComponent} from "../registration-edit-dialog/registration-edit-dialog.component";
import {RegistrationCancelDialogComponent} from "../registration-cancel-dialog/registration-cancel-dialog.component";
import {AuthService, LoadingBarComponent} from "@open-event-workspace/shared";
import {MatDialog} from "@angular/material/dialog";
import {HotToastService} from "@ngxpert/hot-toast";
import {TranslatePipe, TranslateService} from "@ngx-translate/core";
import {RegistrationParticipateAccountDialogComponent} from "../registration-participate-account-dialog/registration-participate-account-dialog.component";
import {RegistrationParticipateManualDialogComponent} from "../registration-participate-manual-dialog/registration-participate-manual-dialog.component";
import {Participant, ParticipantAddRequest, ParticipantDetails, ParticipateRequest, ParticipateResponse, RegistrationDetails, RegistrationInfo} from "@open-event-workspace/core";
import {MatButton} from "@angular/material/button";
import {DatePipe} from "@angular/common";
import {MatIcon} from "@angular/material/icon";
import {MatCard, MatCardActions, MatCardContent, MatCardHeader} from "@angular/material/card";
import {MatDivider} from "@angular/material/divider";
import {Roles} from "../../../shared/roles";
import {RegistrationService} from "@open-event-workspace/app";

@Component({
  selector: 'app-registration-moderation',
  templateUrl: './registration-moderation.component.html',
  styleUrl: './registration-moderation.component.scss',
  imports: [
    MatIcon,
    TranslatePipe,
    MatCard,
    MatTableModule,
    DatePipe,
    MatCardHeader,
    MatDivider,
    MatCardContent,
    MatSort,
    MatSortHeader,
    MatButton,
    MatCardActions,
    LoadingBarComponent
  ],
  standalone: true
})
export class RegistrationModerationComponent {


  @Input()
  set data(value: RegistrationInfo) {
    this.registration = value
    this.handleRegistrationChanged()
  }

  registration: RegistrationInfo | undefined
  reloading: boolean = false
  adminOrManager: boolean = false
  accepted: Participant[] = []
  waitList: Participant[] = []
  userParticipant: Participant | undefined
  displayedColumns: string[] = ['rank', 'size', 'status', 'waitinglist', 'name', 'email', 'phone', 'mobile', 'timestamp', 'action']
  dataSource = new MatTableDataSource<ParticipantDetails>([])

  constructor(
    private service: RegistrationService,
    private dialog: MatDialog,
    private hotToast: HotToastService,
    private translation: TranslateService,
    private authService: AuthService
  ) {
  }

  ngOnInit() {
    this.adminOrManager = this.authService.hasRole(Roles.REGISTRATION_MANAGE, Roles.REGISTRATION_ADMIN)
    let principal = this.authService.getPrincipal()
    if (principal) this.adminOrManager = principal.roles.find(r => r === 'openevent.registration.manage' || r === 'openevent.registration.admin') != null
  }

  @ViewChild(MatSort) sort: MatSort | undefined;

  ngAfterViewInit() {
    if (this.sort) this.dataSource.sort = this.sort
  }


  editParticipant(part: Participant) {
    if (this.reloading || !this.registration) return

    let dialogRef = this.dialog.open(RegistrationEditDialogComponent, {data: part})
    dialogRef.afterClosed().subscribe(request => {
      if (request) this.requestEditParticipant(part, request)
    })
  }

  removeParticipant(part: Participant) {
    if (this.reloading || !this.registration) return

    let dialogRef = this.dialog.open(RegistrationCancelDialogComponent, {data: part})
    dialogRef.afterClosed().subscribe(request => {
      if (request) this.requestRemoveParticipant(part)
    })
  }

  private requestEditParticipant(participant: Participant, request: ParticipateRequest) {
    if (this.reloading || !this.registration) return
    this.reloading = true
    this.service.moderationChangeParticipant(this.registration.registration.id, participant.id, request).subscribe(r => this.handleParticipateResponse(r))
  }

  private requestRemoveParticipant(participant: Participant) {
    if (this.reloading || !this.registration) return
    this.reloading = true
    this.service.moderationRemoveParticipant(this.registration.registration.id, participant.id).subscribe(r => this.handleParticipateResponse(r))
  }

  private handleParticipateResponse(response: ParticipateResponse) {
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
    this.reloadDetails()
  }

  private updateParticipants(participants: Participant[]) {
    if (!this.registration) return
    this.registration.participants = participants
    this.updateData()
  }

  private updateData() {
    if (this.registration) {
      this.accepted = this.registration.participants.filter(p => !p.waitingList)
      this.waitList = this.registration.participants.filter(p => p.waitingList)
      this.userParticipant = this.registration.participants.find(p => p.author.email === this.authService.getPrincipal()?.email)
    }
  }

  participateAccount() {
    if (!this.registration) return
    if (this.reloading) return
    let dialogRef = this.dialog.open(RegistrationParticipateAccountDialogComponent)
    dialogRef.afterClosed().subscribe(request => {
      if (request) this.requestParticipateAccount(request)
    })
  }

  private requestParticipateAccount(request: any) {

  }

  participateManual() {
    if (!this.registration) return
    if (this.reloading) return
    let dialogRef = this.dialog.open(RegistrationParticipateManualDialogComponent)
    dialogRef.afterClosed().subscribe(request => {
      if (request) this.requestParticipateManual(request)
    })
  }


  private requestParticipateManual(request: ParticipantAddRequest) {
    if (!this.registration) return
    if (this.reloading) return
    this.reloading = true
    this.service.moderationParticipateManual(this.registration.registration.id, request).subscribe(r => this.handleParticipateResponse(r))
  }

  private handleRegistrationChanged() {
    this.reloadDetails()
  }

  private reloadDetails() {
    if (!this.registration) return
    this.reloading = true
    this.service.getDetails(this.registration.registration.id).subscribe({
      next: value => this.handleDetailsData(value),
      error: err => this.handleError(err)
    })
  }

  private handleDetailsData(value: RegistrationDetails) {
    this.dataSource.data = value.participants
    this.reloading = false
  }

  private handleError(err: any) {
    this.hotToast.error("Something went wrong")
    this.reloading = false
  }
}
