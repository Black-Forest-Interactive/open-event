import { Component, computed, effect, inject, resource, signal } from '@angular/core'
import { ActivatedRoute } from '@angular/router'
import { TranslateService } from '@ngx-translate/core'
import { MatDialog } from '@angular/material/dialog'
import { HotToastService } from '@ngxpert/hot-toast'
import { EventService, ExternalParticipantAddRequest, ExternalParticipantChangeResponse } from '@open-event/external'
import { LoadingBarComponent, toPromise } from '@open-event/shared'
import { EventInfoComponent } from '../event-info/event-info.component'
import { toSignal } from '@angular/core/rxjs-interop'
import { map } from 'rxjs'
import { RequestParticipationDialogComponent } from '../../participant/request-participation-dialog/request-participation-dialog.component'
import { RequestParticipationResponseDialogComponent } from '../../participant/request-participation-response-dialog/request-participation-response-dialog.component'
import { EventActionComponent } from '../event-action/event-action.component'
import { MatCard } from '@angular/material/card'

@Component({
  selector: 'app-event',
  imports: [LoadingBarComponent, EventInfoComponent, EventActionComponent, MatCard],
  templateUrl: './event.component.html',
  styleUrl: './event.component.scss'
})
export class EventComponent {
  readonly processing = signal(false)
  readonly status = signal('')
  private service = inject(EventService)
  private translate = inject(TranslateService)
  private route = inject(ActivatedRoute)
  private dialog = inject(MatDialog)
  private hotToast = inject(HotToastService)
  private eventId = toSignal(this.route.paramMap.pipe(map((p) => p.get('id') ?? undefined)))
  private lang = toSignal(this.route.queryParams.pipe(map((p) => p['lang'])))
  private eventResource = resource({
    params: this.eventId,
    loader: (p) => (p.params ? toPromise(this.service.getEvent(p.params), p.abortSignal) : Promise.resolve(undefined))
  })

  readonly event = computed(() => this.eventResource.value())
  readonly loading = this.eventResource.isLoading
  readonly error = this.eventResource.error

  constructor() {
    this.translate.setDefaultLang('en')

    effect(() => {
      const lang = this.lang()
      if (lang) this.translate.use(lang)
    })

    effect(() => {
      if (!this.error()) return
      this.translate.get('event.message.error').subscribe((t) => this.hotToast.error(t))
    })

    effect(() => {
      const eventId = this.eventId()
      if (eventId) {
        const status = sessionStorage.getItem(eventId)
        if (status) this.status.set(status)
      }
    })
  }

  participate() {
    if (this.processing()) return
    const dialogRef = this.dialog.open(RequestParticipationDialogComponent, { disableClose: true, width: 'min(480px, 96vw)', maxWidth: '96vw' })
    dialogRef.afterClosed().subscribe((request) => {
      if (request) this.requestParticipate(request)
    })
  }

  private requestParticipate(request: ExternalParticipantAddRequest) {
    if (!this.isValid(request)) return
    const id = this.eventId()
    if (!id) return
    this.processing.set(true)
    this.service.requestParticipation(id, request).subscribe({
      next: (value) => this.handleParticipateResponse(value),
      error: () => this.translate.get('event.message.error').subscribe((t) => this.hotToast.error(t))
    })
  }

  private isValid(request: ExternalParticipantAddRequest) {
    if (request.size <= 0) return false
    return request.email.length > 0 || request.mobile.length > 0 || request.phone.length > 0
  }

  private handleParticipateResponse(response: ExternalParticipantChangeResponse) {
    if (response.status == 'FAILED') {
      this.translate.get('registration.message.error').subscribe((t) => this.hotToast.error(t))
    } else {
      this.dialog.open(RequestParticipationResponseDialogComponent, { width: 'min(480px, 96vw)', maxWidth: '96vw' })
      this.processing.set(false)
      this.status.set(response.status)
      const eventId = this.eventId()
      if (eventId) sessionStorage.setItem(eventId, response.status)
    }
    this.eventResource.reload()
  }
}
