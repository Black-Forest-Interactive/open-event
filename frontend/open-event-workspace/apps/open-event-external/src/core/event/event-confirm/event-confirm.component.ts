import {Component, computed, effect, resource, signal} from '@angular/core';
import {EventInfoComponent} from "../event-info/event-info.component";
import {ConfirmationCodeComponent, LoadingBarComponent, toPromise} from "@open-event-workspace/shared";
import {MatCard} from "@angular/material/card";
import {MatToolbar} from "@angular/material/toolbar";
import {ActivatedRoute, ParamMap, Params, RouterLink} from "@angular/router";
import {EventService, ExternalParticipantConfirmRequest, ExternalParticipantConfirmResponse} from "@open-event-workspace/external";
import {TranslatePipe, TranslateService} from "@ngx-translate/core";
import {MatDialog} from "@angular/material/dialog";
import {HotToastService} from "@ngxpert/hot-toast";
import {takeUntilDestroyed} from "@angular/core/rxjs-interop";
import {ConfirmParticipationResponseDialogComponent} from "../../participant/confirm-participation-response-dialog/confirm-participation-response-dialog.component";

@Component({
  selector: 'app-event-confirm',
  imports: [
    EventInfoComponent,
    LoadingBarComponent,
    MatCard,
    MatToolbar,
    RouterLink,
    ConfirmationCodeComponent,
    TranslatePipe
  ],
  templateUrl: './event-confirm.component.html',
  styleUrl: './event-confirm.component.scss'
})
export class EventConfirmComponent {

  eventId = signal<string | undefined>(undefined)
  eventResource = resource({
    request: this.eventId,
    loader: (param) => {
      return toPromise(this.service.getEvent(param.request))
    }
  })


  event = computed(this.eventResource.value ?? undefined)
  loading = this.eventResource.isLoading
  error = this.eventResource.error

  processing = signal(false)
  status = signal('')

  participantId = signal<string | undefined>(undefined)
  confirmationPossible = computed(() => this.participantId() && (this.status() === 'UNCONFIRMED' || this.status() === '' || this.status() === 'FAILED'))

  constructor(
    private service: EventService,
    private translate: TranslateService,
    private route: ActivatedRoute,
    private dialog: MatDialog,
    private hotToast: HotToastService,
  ) {
    effect(() => {
      this.handleError(this.error())
    });
    this.translate.setDefaultLang('en')
    this.route.queryParams.pipe(takeUntilDestroyed()).subscribe(p => this.handleQueryParams(p))
    this.route.paramMap.pipe(takeUntilDestroyed()).subscribe(p => this.handleParams(p))
    effect(() => {
      let eventId = this.eventId()
      if (eventId) {
        let status = sessionStorage.getItem(eventId)
        if (status) this.status.set(status)
      }
    });
  }

  private handleParams(p: ParamMap) {
    let idParam = p.get('id')
    if (!idParam) return
    this.eventId.set(idParam)
  }

  private handleQueryParams(p: Params) {
    let lang = p['lang']
    if (lang) this.translate.use(lang)

    let participantId = p['pid']
    this.participantId.set(participantId)
  }

  private handleError(err: any) {
    if (!err) return
    this.hotToast.error(err)
  }

  onCodeComplete(code: string) {
    let participantId = this.participantId()
    if (!participantId) return
    this.confirm(code, participantId)
  }


  private confirm(code: string, participantId: string) {
    if (this.processing()) return
    let id = this.eventId()
    if (!id) return
    let request = new ExternalParticipantConfirmRequest(code)
    this.processing.set(true)
    this.service.confirmParticipation(id, participantId, request).subscribe({
      next: value => this.handleConfirmationResponse(value),
      error: err => this.handleError(err)
    })
  }


  private handleConfirmationResponse(response: ExternalParticipantConfirmResponse) {
    let participant = response.participant
    if (response.status == 'FAILED' || !participant) {
      this.translate.get('registration.dialog.response.error').subscribe(v => this.handleError(v))
    } else {
      this.dialog.open(ConfirmParticipationResponseDialogComponent, {data: participant})
      this.processing.set(false)
      this.status.set(response.status)
      let eventId = this.eventId()
      if (eventId) sessionStorage.setItem(eventId, response.status)
    }
    this.eventResource.reload()
  }
}
