import {Component, computed, effect, resource, signal} from '@angular/core';
import {MatToolbar} from "@angular/material/toolbar";
import {ParticipantAddRequest} from "@open-event-workspace/core";
import {ActivatedRoute, ParamMap, Params, RouterLink} from "@angular/router";
import {TranslateService} from "@ngx-translate/core";
import {MatDialog} from "@angular/material/dialog";
import {HotToastService} from "@ngxpert/hot-toast";
import {EventService, ExternalParticipantAddRequest, ExternalParticipantChangeResponse} from "@open-event-workspace/external";
import {LoadingBarComponent, toPromise} from "@open-event-workspace/shared";
import {EventInfoComponent} from "../event-info/event-info.component";
import {takeUntilDestroyed} from "@angular/core/rxjs-interop";
import {RequestParticipationDialogComponent} from "../../participant/request-participation-dialog/request-participation-dialog.component";
import {RequestParticipationResponseDialogComponent} from "../../participant/request-participation-response-dialog/request-participation-response-dialog.component";
import {EventActionComponent} from "../event-action/event-action.component";
import {MatCard} from "@angular/material/card";


@Component({
  selector: 'app-event',
  imports: [
    MatToolbar,
    RouterLink,
    LoadingBarComponent,
    EventInfoComponent,
    EventActionComponent,
    MatCard
  ],
  templateUrl: './event.component.html',
  styleUrl: './event.component.scss'
})
export class EventComponent {


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
  }

  private handleError(err: any) {
    if (!err) return
    this.hotToast.error("Something went wrong")
  }

  participate() {
    if (this.processing()) return
    let dialogRef = this.dialog.open(RequestParticipationDialogComponent, {disableClose: true})
    dialogRef.afterClosed().subscribe(request => {
      if (request) this.requestParticipate(request)
    })
  }

  private requestParticipate(request: ExternalParticipantAddRequest) {
    if (!this.isValid(request)) return
    let id = this.eventId()
    if (!id) return
    this.processing.set(true)
    this.service.requestParticipation(id, request).subscribe({
      next: value => this.handleParticipateResponse(value),
      error: err => this.handleError(err)
    })
  }

  private isValid(request: ParticipantAddRequest) {
    if (request.size <= 0) return false
    return request.email.length > 0 || request.mobile.length > 0 || request.phone.length > 0
  }

  private handleParticipateResponse(response: ExternalParticipantChangeResponse) {
    if (response.status == 'FAILED') {
      this.translate.get('registration.dialog.response.error').subscribe(v => this.handleError(v))
    } else {
      this.showRequestParticipationResponseDialog()
      this.processing.set(false)
      this.status.set(response.status)
      let eventId = this.eventId()
      if (eventId) sessionStorage.setItem(eventId, response.status)
    }
    this.eventResource.reload()
  }

  private showRequestParticipationResponseDialog() {
    this.dialog.open(RequestParticipationResponseDialogComponent)
  }

}
