import { Component, computed, effect, inject, resource, signal } from '@angular/core'
import { EventInfoComponent } from '../event-info/event-info.component'
import { ConfirmationCodeComponent, LoadingBarComponent, toPromise } from '@open-event/shared'
import { MatCard } from '@angular/material/card'
import { MatToolbar } from '@angular/material/toolbar'
import { ActivatedRoute, RouterLink } from '@angular/router'
import { EventService, ExternalParticipantConfirmRequest, ExternalParticipantConfirmResponse } from '@open-event/external'
import { TranslatePipe, TranslateService } from '@ngx-translate/core'
import { MatDialog } from '@angular/material/dialog'
import { HotToastService } from '@ngxpert/hot-toast'
import { toSignal } from '@angular/core/rxjs-interop'
import { map } from 'rxjs'
import { ConfirmParticipationResponseDialogComponent } from '../../participant/confirm-participation-response-dialog/confirm-participation-response-dialog.component'

@Component({
  selector: 'app-event-confirm',
  imports: [EventInfoComponent, LoadingBarComponent, MatCard, MatToolbar, RouterLink, TranslatePipe, ConfirmationCodeComponent],
  templateUrl: './event-confirm.component.html',
  styleUrl: './event-confirm.component.scss'
})
export class EventConfirmComponent {
  private service = inject(EventService)
  private translate = inject(TranslateService)
  private route = inject(ActivatedRoute)
  private dialog = inject(MatDialog)
  private hotToast = inject(HotToastService)

  private eventId = toSignal(this.route.paramMap.pipe(map(p => p.get('id') ?? undefined)))
  private lang = toSignal(this.route.queryParams.pipe(map(p => p['lang'] as string | undefined)))
  private participantId = toSignal(this.route.queryParams.pipe(map(p => p['pid'] as string | undefined)))

  private processing = signal(false)
  private status = signal('')

  private eventResource = resource({
    params: this.eventId,
    loader: (p) => p.params ? toPromise(this.service.getEvent(p.params), p.abortSignal) : Promise.resolve(undefined)
  })

  private settingsResource = resource({
    loader: (p) => toPromise(this.service.getSettings(), p.abortSignal)
  })

  readonly event = computed(() => this.eventResource.value())
  readonly settings = computed(() => this.settingsResource.value())
  readonly loading = computed(() => this.eventResource.isLoading() || this.settingsResource.isLoading())
  readonly error = computed(() => this.eventResource.error() || this.settingsResource.error())
  readonly confirmationPossible = computed(() => {
    if (!this.participantId()) return false
    const s = this.status()
    return s === 'UNCONFIRMED' || s === '' || s === 'FAILED'
  })
  readonly requireValidateCode = computed(() => this.settings()?.requireValidateCode ?? true)

  constructor() {
    this.translate.setDefaultLang('en')

    effect(() => {
      const lang = this.lang()
      if (lang) this.translate.use(lang)
    })

    effect(() => {
      if (!this.error()) return
      this.translate.get('event.message.error').subscribe(t => this.hotToast.error(t))
    })

    effect(() => {
      const eventId = this.eventId()
      if (eventId) {
        const status = sessionStorage.getItem(eventId)
        if (status) this.status.set(status)
      }
    })

    effect(() => {
      const settings = this.settings()
      const participantId = this.participantId()
      if (!this.event() || !settings || !participantId) return
      if (!settings.requireValidateCode && this.confirmationPossible()) this.confirm('000000', participantId)
    })
  }

  onCodeComplete(code: string) {
    const participantId = this.participantId()
    if (!participantId) return
    this.confirm(code, participantId)
  }

  private confirm(code: string, participantId: string) {
    if (this.processing()) return
    const id = this.eventId()
    if (!id) return
    const request = new ExternalParticipantConfirmRequest(code)
    this.processing.set(true)
    this.service.confirmParticipation(id, participantId, request).subscribe({
      next: (value) => this.handleConfirmationResponse(value),
      error: () => this.translate.get('event.message.error').subscribe(t => this.hotToast.error(t))
    })
  }

  private handleConfirmationResponse(response: ExternalParticipantConfirmResponse) {
    const participant = response.participant
    if (response.status == 'FAILED' || !participant) {
      this.translate.get('registration.message.error').subscribe(t => this.hotToast.error(t))
    } else {
      this.dialog.open(ConfirmParticipationResponseDialogComponent, { data: participant, width: 'min(480px, 96vw)', maxWidth: '96vw' })
      this.processing.set(false)
      this.status.set(response.status)
      const eventId = this.eventId()
      if (eventId) sessionStorage.setItem(eventId, response.status)
    }
    this.eventResource.reload()
  }

}
