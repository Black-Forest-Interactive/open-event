import { Component, computed, inject, model, signal } from '@angular/core'
import { MatDialog } from '@angular/material/dialog'
import { HotToastService } from '@ngxpert/hot-toast'
import { RegistrationParticipateDialogComponent } from '../registration-participate-dialog/registration-participate-dialog.component'
import { TranslatePipe, TranslateService } from '@ngx-translate/core'
import { RegistrationEditDialogComponent } from '../registration-edit-dialog/registration-edit-dialog.component'
import { RegistrationCancelDialogComponent } from '../registration-cancel-dialog/registration-cancel-dialog.component'
import { AuthService, LoadingBarComponent } from '@open-event/shared'
import { ParticipateRequest, ParticipateResponse, RegistrationInfo } from '@open-event/core'
import { RegistrationStatusComponent } from '@open-event/ui'
import { MatButton } from '@angular/material/button'
import { NgTemplateOutlet } from '@angular/common'
import { MatIcon } from '@angular/material/icon'
import { MatDivider } from '@angular/material/divider'
import { AccountComponent } from '../../account/account/account.component'
import { RegistrationService } from '@open-event/portal'

@Component({
  selector: 'portal-registration-details',
  templateUrl: './registration-details.component.html',
  styleUrl: './registration-details.component.scss',
  imports: [MatIcon, TranslatePipe, RegistrationStatusComponent, MatDivider, NgTemplateOutlet, MatButton, AccountComponent, LoadingBarComponent],
  standalone: true
})
export class RegistrationDetailsComponent {
  data = model.required<RegistrationInfo>()
  readonly participants = computed(() => this.data().participants)
  readonly accepted = computed(() => this.participants().filter((p) => !p.waitingList))
  readonly waitList = computed(() => this.participants().filter((p) => p.waitingList))
  reloading = signal(false)
  private service = inject(RegistrationService)
  private dialog = inject(MatDialog)
  private hotToast = inject(HotToastService)
  private translation = inject(TranslateService)
  private authService = inject(AuthService)
  readonly userParticipant = computed(() => this.participants().find((p) => p.author.email === this.authService.getPrincipal()?.email))

  participateSelf() {
    if (this.reloading()) return
    const dialogRef = this.dialog.open(RegistrationParticipateDialogComponent)
    dialogRef.afterClosed().subscribe((request) => {
      if (request) this.requestParticipateSelf(request)
    })
  }

  editSelf() {
    if (this.reloading()) return

    const dialogRef = this.dialog.open(RegistrationEditDialogComponent, {
      data: this.userParticipant
    })
    dialogRef.afterClosed().subscribe((request) => {
      if (request) this.requestEditSelf(request)
    })
  }

  cancelSelf() {
    if (this.reloading()) return

    const dialogRef = this.dialog.open(RegistrationCancelDialogComponent)
    dialogRef.afterClosed().subscribe((request) => {
      if (request) this.requestCancelSelf()
    })
  }

  private requestParticipateSelf(request: ParticipateRequest) {
    if (this.reloading()) return
    this.reloading.set(true)
    this.service.addParticipant(this.data().registration.id, request).subscribe((r) => this.handleParticipateResponse(r))
  }

  private requestEditSelf(request: ParticipateRequest) {
    if (this.reloading()) return
    this.reloading.set(true)
    this.service.changeParticipant(this.data().registration.id, request).subscribe((r) => this.handleParticipateResponse(r))
  }

  private requestCancelSelf() {
    if (this.reloading()) return
    this.reloading.set(true)
    this.service.removeParticipant(this.data().registration.id).subscribe((r) => this.handleParticipateResponse(r))
  }

  private handleParticipateResponse(response: ParticipateResponse) {
    this.data.set({ ...this.data(), participants: response.participants })
    switch (response.status) {
      case 'ACCEPTED':
        this.translation.get('registration.message.accepted').subscribe((msg) => this.hotToast.success(msg))
        break
      case 'WAITING_LIST_DECREASE_SIZE':
      case 'WAITING_LIST':
        this.translation.get('registration.message.waiting').subscribe((msg) => this.hotToast.info(msg))
        break
      case 'DECLINED':
        this.translation.get('registration.message.declined').subscribe((msg) => this.hotToast.warning(msg))
        break
      case 'FAILED':
        this.translation.get('registration.message.failed').subscribe((msg) => this.hotToast.error(msg))
        break
    }
    this.reloading.set(false)
  }
}
