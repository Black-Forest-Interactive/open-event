import {Component} from '@angular/core';
import {ActivatedRoute, ParamMap, RouterLink} from "@angular/router";
import {TranslatePipe, TranslateService} from "@ngx-translate/core";
import {RegistrationParticipateManualDialogComponent} from "../../registration/registration-participate-manual-dialog/registration-participate-manual-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {HotToastService} from "@ngxpert/hot-toast";
import {ParticipantAddRequest, RegistrationStatusComponent, SharedParticipateResponse, ShareInfo, ShareService} from "@open-event-workspace/core";
import {MatToolbar} from "@angular/material/toolbar";
import {MatProgressBar} from "@angular/material/progress-bar";
import {MatCard, MatCardActions, MatCardContent, MatCardHeader, MatCardImage, MatCardSubtitle, MatCardTitle} from "@angular/material/card";
import {MatIcon} from "@angular/material/icon";
import {DatePipe} from "@angular/common";
import {MatDivider} from "@angular/material/divider";
import {MatChip, MatChipListbox} from "@angular/material/chips";
import {LocationMapComponent} from "../../location/location-map/location-map.component";
import {MatAnchor, MatButton} from "@angular/material/button";
import {MatProgressSpinner} from "@angular/material/progress-spinner";

@Component({
  selector: 'app-share-info',
  templateUrl: './share-info.component.html',
  styleUrl: './share-info.component.scss',
  imports: [
    MatToolbar,
    RouterLink,
    MatProgressBar,
    MatCard,
    MatCardHeader,
    MatCardSubtitle,
    MatCardTitle,
    MatIcon,
    DatePipe,
    MatCardImage,
    MatDivider,
    MatCardContent,
    MatCardActions,
    MatChipListbox,
    MatChip,
    LocationMapComponent,
    RegistrationStatusComponent,
    MatButton,
    MatProgressSpinner,
    TranslatePipe,
    MatAnchor
  ],
  standalone: true
})
export class ShareInfoComponent {
  reloading: boolean = false
  processing: boolean = false
  participated: boolean = false
  info: ShareInfo | undefined
  id: string | undefined

  constructor(
    private service: ShareService,
    private translate: TranslateService,
    private route: ActivatedRoute,
    private dialog: MatDialog,
    private hotToast: HotToastService,
  ) {
  }

  ngOnInit() {
    this.translate.setDefaultLang('en')
    this.reloading = true
    let lang = this.route.snapshot.queryParams['lang'] ?? 'en'
    this.translate.use(lang)
    this.route.paramMap.subscribe(p => this.handleParams(p))
  }


  private handleParams(p: ParamMap) {
    let idParam = p.get('id')
    if (!idParam) return
    this.id = idParam
    this.reload()
  }


  private handleData(value: ShareInfo) {
    this.info = value
    this.reloading = false
  }

  private handleError(err: any) {
    this.hotToast.error("Something went wrong")
    this.reloading = false
    this.processing = false
  }

  participate() {
    if (this.processing) return
    this.processing = true
    let dialogRef = this.dialog.open(RegistrationParticipateManualDialogComponent, {disableClose: true})
    dialogRef.afterClosed().subscribe(request => {
      if (request) this.requestParticipateManual(request)
    })
  }

  private requestParticipateManual(request: ParticipantAddRequest) {
    if (!this.isValid(request)) return
    if (!this.id) return
    this.service.addParticipant(this.id, request).subscribe({
      next: value => this.handleParticipateResponse(value),
      error: err => this.handleError(err)
    })
  }

  private isValid(request: ParticipantAddRequest) {
    if (request.size <= 0) return false
    return request.email.length > 0 || request.mobile.length > 0 || request.phone.length > 0
  }

  private handleParticipateResponse(response: SharedParticipateResponse) {
    switch (response.status) {
      case 'ACCEPTED':
        this.translate.get('registration.message.accepted').subscribe(msg => this.hotToast.success(msg))
        this.participated = true
        break;
      case 'WAITING_LIST_DECREASE_SIZE':
      case 'WAITING_LIST':
        this.translate.get('registration.message.waiting').subscribe(msg => this.hotToast.info(msg))
        this.participated = true
        break;
      case 'DECLINED':
        this.translate.get('registration.message.declined').subscribe(msg => this.hotToast.warning(msg))
        break;
      case 'FAILED':
        this.translate.get('registration.message.failed').subscribe(msg => this.hotToast.error(msg))
        break;
    }

    this.processing = false
    this.reload()
  }

  private reload() {
    if (!this.id) return
    this.reloading = true
    this.service.getInfo(this.id).subscribe({
      next: value => this.handleData(value),
      error: err => this.handleError(err)
    })
  }
}
