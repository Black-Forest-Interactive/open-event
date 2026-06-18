import { Component, computed, inject, resource, signal } from '@angular/core'
import { ActivatedRoute } from '@angular/router'
import { MatDialog } from '@angular/material/dialog'
import { MatBottomSheet } from '@angular/material/bottom-sheet'
import { HotToastService } from '@ngxpert/hot-toast'
import { TranslateService } from '@ngx-translate/core'
import { EventDetailsHeaderComponent } from '../event-details-header/event-details-header.component'
import { EventDetailsInfoComponent } from '../event-details-info/event-details-info.component'
import { EventDetailsLocationComponent } from '../event-details-location/event-details-location.component'
import { EventHostBlockComponent } from '../event-host-block/event-host-block.component'
import { EventGuestListComponent } from '../event-guest-list/event-guest-list.component'
import { EventParticipantsStackComponent } from '../event-participants-stack/event-participants-stack.component'
import { EventBookbarComponent } from '../event-bookbar/event-bookbar.component'
import { RegistrationDetailsComponent } from '../../registration/registration-details/registration-details.component'
import { RegistrationParticipateSheetComponent } from '../../registration/registration-participate-sheet/registration-participate-sheet.component'
import { RegistrationCancelDialogComponent } from '../../registration/registration-cancel-dialog/registration-cancel-dialog.component'
import { ShareDetailsComponent } from '../../share/share-details/share-details.component'
import { EventService, RegistrationService } from '@open-event/portal'
import { AuthService, LoadingBarComponent, toPromise } from '@open-event/shared'
import { ParticipateRequest, ParticipateResponse } from '@open-event/core'
import { EventDetailsBannerComponent } from '../event-details-banner/event-details-banner.component'
import { MatCard } from '@angular/material/card'
import { toSignal } from '@angular/core/rxjs-interop'
import { map } from 'rxjs/operators'

@Component({
  selector: 'portal-event-details',
  templateUrl: './event-details.component.html',
  styleUrls: ['./event-details.component.scss'],
  imports: [
    EventDetailsHeaderComponent,
    EventDetailsInfoComponent,
    EventDetailsLocationComponent,
    EventHostBlockComponent,
    EventGuestListComponent,
    EventParticipantsStackComponent,
    EventBookbarComponent,
    RegistrationDetailsComponent,
    ShareDetailsComponent,
    LoadingBarComponent,
    EventDetailsBannerComponent,
    MatCard
  ],
  standalone: true
})
export class EventDetailsComponent {
  readonly registration = computed(() => this.info()?.registration)
  readonly canEdit = computed(() => this.info()?.canEdit ?? false)
  readonly share = computed(() => this.info()?.share)
  readonly location = computed(() => this.info()?.location)
  readonly registrationReloading = signal(false)
  private route = inject(ActivatedRoute)
  private service = inject(EventService)
  private registrationService = inject(RegistrationService)
  private dialog = inject(MatDialog)
  private bottomSheet = inject(MatBottomSheet)
  private hotToast = inject(HotToastService)
  private translation = inject(TranslateService)
  private authService = inject(AuthService)
  private eventId = toSignal(
    this.route.paramMap.pipe(
      map((p) => {
        const id = p.get('id')
        return id ? +id : undefined
      })
    )
  )
  private infoResource = resource({
    params: this.eventId,
    loader: (p) => (p.params ? toPromise(this.service.getEventInfo(p.params), p.abortSignal) : Promise.resolve(undefined))
  })
  readonly info = computed(() => this.infoResource.value())
  readonly reloading = this.infoResource.isLoading
  readonly isBookmarked = computed(() => this.info()?.bookmarked ?? false)
  readonly userParticipant = computed(() => {
    const email = this.authService.getPrincipal()?.email.toLowerCase()
    return this.registration()?.participants.find((p) => p.author.email.toLowerCase() === email)
  })

  reload() {
    this.infoResource.reload()
  }

  setSharingEnabled(enabled: boolean) {
    const id = this.eventId()
    if (!id) return
    this.service.setShared(id, enabled).subscribe((d) => this.infoResource.set(d))
  }

  toggleBookmark() {
    const id = this.eventId()
    if (!id) return
    const obs = this.isBookmarked() ? this.service.clearBookmarked(id) : this.service.setBookmarked(id)
    obs.subscribe({ next: (info) => this.infoResource.set(info) })
  }

  participateSelf() {
    if (this.registrationReloading()) return
    const info = this.info()
    const registration = this.registration()
    if (!info || !registration) return
    const sheetRef = this.bottomSheet.open(RegistrationParticipateSheetComponent, {
      data: {
        info,
        titleKey: 'registration.dialog.accept.title',
        submit: (request: ParticipateRequest) => this.registrationService.addParticipant(registration.registration.id, request)
      }
    })
    sheetRef.afterDismissed().subscribe((response) => {
      if (response) this.handleParticipateResponse(response)
    })
  }

  editSelf() {
    if (this.registrationReloading()) return
    const info = this.info()
    const registration = this.registration()
    if (!info || !registration) return
    const sheetRef = this.bottomSheet.open(RegistrationParticipateSheetComponent, {
      data: {
        info,
        participant: this.userParticipant(),
        titleKey: 'registration.dialog.edit.title',
        submit: (request: ParticipateRequest) => this.registrationService.changeParticipant(registration.registration.id, request)
      }
    })
    sheetRef.afterDismissed().subscribe((response) => {
      if (response) this.handleParticipateResponse(response)
    })
  }

  cancelSelf() {
    if (this.registrationReloading()) return
    const dialogRef = this.dialog.open(RegistrationCancelDialogComponent)
    dialogRef.afterClosed().subscribe((request) => {
      if (request) this.requestCancelSelf()
    })
  }

  private requestCancelSelf() {
    const registration = this.registration()
    if (this.registrationReloading() || !registration) return
    this.registrationReloading.set(true)
    this.registrationService.removeParticipant(registration.registration.id).subscribe((r) => this.handleParticipateResponse(r))
  }

  private handleParticipateResponse(response: ParticipateResponse) {
    const current = this.info()
    if (current && current.registration) {
      this.infoResource.set({ ...current, registration: { ...current.registration, participants: response.participants } })
    }
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
    this.registrationReloading.set(false)
  }
}
