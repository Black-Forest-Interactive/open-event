import {Component} from '@angular/core';
import {ShareService} from "../model/share.service";
import {ActivatedRoute, ParamMap} from "@angular/router";
import {SharedParticipateResponse, ShareInfo} from "../model/share-api";
import {TranslateService} from "@ngx-translate/core";
import {
  RegistrationParticipateManualDialogComponent
} from "../../registration/registration-participate-manual-dialog/registration-participate-manual-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {HotToastService} from "@ngxpert/hot-toast";
import {ParticipantAddRequest} from "../../registration/model/registration-api";

@Component({
    selector: 'app-share-info',
    templateUrl: './share-info.component.html',
    styleUrl: './share-info.component.scss',
    standalone: false
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
