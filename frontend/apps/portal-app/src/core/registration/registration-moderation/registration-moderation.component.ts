import { Component, computed, inject, input, resource, signal } from '@angular/core'
import { RegistrationEditDialogComponent } from '../registration-edit-dialog/registration-edit-dialog.component'
import { RegistrationCancelDialogComponent } from '../registration-cancel-dialog/registration-cancel-dialog.component'
import { AuthService, LoadingBarComponent, toPromise } from '@open-event/shared'
import { MatDialog } from '@angular/material/dialog'
import { HotToastService } from '@ngxpert/hot-toast'
import { TranslatePipe, TranslateService } from '@ngx-translate/core'
import { RegistrationParticipateManualDialogComponent } from '../registration-participate-manual-dialog/registration-participate-manual-dialog.component'
import { Participant, ParticipantAddRequest, ParticipateRequest, ParticipateResponse, RegistrationInfo } from '@open-event/core'
import { MatButton, MatIconButton } from '@angular/material/button'
import { DatePipe, NgStyle } from '@angular/common'
import { MatIcon } from '@angular/material/icon'
import { MatCard } from '@angular/material/card'
import { MatDivider } from '@angular/material/divider'
import { MatTooltip } from '@angular/material/tooltip'
import { Roles } from '../../../shared/roles'
import { RegistrationService } from '@open-event/portal'

@Component({
  selector: 'portal-registration-moderation',
  templateUrl: './registration-moderation.component.html',
  styleUrl: './registration-moderation.component.scss',
  imports: [MatIcon, MatIconButton, MatTooltip, TranslatePipe, MatCard, DatePipe, NgStyle, MatDivider, MatButton, LoadingBarComponent],
  standalone: true
})
export class RegistrationModerationComponent {
  data = input<RegistrationInfo | undefined>()
  private service = inject(RegistrationService)
  private dialog = inject(MatDialog)
  private hotToast = inject(HotToastService)
  private translation = inject(TranslateService)
  private authService = inject(AuthService)
  readonly adminOrManager = computed(() => this.authService.hasRole(Roles.REGISTRATION_MANAGE, Roles.REGISTRATION_ADMIN))
  private reloading = signal(false)
  private detailsResource = resource({
    params: this.data,
    loader: (p) => (p.params ? toPromise(this.service.getDetails(p.params.registration.id), p.abortSignal) : Promise.resolve(undefined))
  })

  readonly loading = computed(() => this.reloading() || this.detailsResource.isLoading())
  readonly participants = computed(() => this.detailsResource.value()?.participants ?? [])

  statusStyle(status: string): Record<string, string> {
    switch (status) {
      case 'ACCEPTED':
        return { background: 'var(--mat-sys-primary)', color: 'var(--mat-sys-on-primary)' }
      case 'WAITING_LIST':
      case 'WAITING_LIST_DECREASE_SIZE':
        return { background: 'var(--mat-sys-tertiary)', color: 'var(--mat-sys-on-tertiary)' }
      case 'DECLINED':
      case 'FAILED':
        return { background: 'var(--mat-sys-error)', color: 'var(--mat-sys-on-error)' }
      default:
        return { background: 'var(--mat-sys-surface-variant)', color: 'var(--mat-sys-on-surface-variant)' }
    }
  }

  editParticipant(part: Participant) {
    if (this.loading() || !this.data()) return
    this.dialog
      .open(RegistrationEditDialogComponent, { data: part })
      .afterClosed()
      .subscribe((request) => {
        if (request) this.requestEditParticipant(part, request)
      })
  }

  removeParticipant(part: Participant) {
    if (this.loading() || !this.data()) return
    this.dialog
      .open(RegistrationCancelDialogComponent, { data: part })
      .afterClosed()
      .subscribe((request) => {
        if (request) this.requestRemoveParticipant(part)
      })
  }

  participateManual() {
    if (!this.data() || this.loading()) return
    this.dialog
      .open(RegistrationParticipateManualDialogComponent)
      .afterClosed()
      .subscribe((request) => {
        if (request) this.requestParticipateManual(request)
      })
  }

  private requestEditParticipant(participant: Participant, request: ParticipateRequest) {
    const reg = this.data()
    if (this.loading() || !reg) return
    this.reloading.set(true)
    this.service.moderationChangeParticipant(reg.registration.id, participant.id, request).subscribe((r) => this.handleParticipateResponse(r))
  }

  private requestRemoveParticipant(participant: Participant) {
    const reg = this.data()
    if (this.loading() || !reg) return
    this.reloading.set(true)
    this.service.moderationRemoveParticipant(reg.registration.id, participant.id).subscribe((r) => this.handleParticipateResponse(r))
  }

  private requestParticipateManual(request: ParticipantAddRequest) {
    const reg = this.data()
    if (!reg || this.loading()) return
    this.reloading.set(true)
    this.service.moderationParticipateManual(reg.registration.id, request).subscribe((r) => this.handleParticipateResponse(r))
  }

  private handleParticipateResponse(response: ParticipateResponse) {
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
    this.detailsResource.reload()
  }
}
