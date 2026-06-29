import { Component, computed, inject, resource } from '@angular/core'
import { Observable } from 'rxjs'
import { MAT_DIALOG_DATA, MatDialogClose, MatDialogContent, MatDialogRef, MatDialogTitle } from '@angular/material/dialog'
import { MatBottomSheet } from '@angular/material/bottom-sheet'
import { MatIcon } from '@angular/material/icon'
import { MatIconButton } from '@angular/material/button'
import { TranslatePipe, TranslateService } from '@ngx-translate/core'
import { HotToastService } from '@ngxpert/hot-toast'
import { Address, AddressChangeRequest, AddressReadAPI, Audience, AudienceReadAPI, Category, CategoryReadAPI, Event, EventChangeRequest, EventInfo, EventReadAPI } from '@open-event/core'
import { AddressService, AudienceService, CategoryService, EventService } from '@open-event/portal'
import { LoadingBarComponent, Page, toPromise } from '@open-event/shared'
import { EventChangeComponent } from '@open-event/ui'
import { EventBroadcastSheetComponent } from '../../announcement/event-broadcast-sheet/event-broadcast-sheet.component'

@Component({
  selector: 'portal-event-edit-dialog',
  templateUrl: './event-edit-dialog.component.html',
  imports: [EventChangeComponent, MatDialogTitle, MatDialogContent, MatDialogClose, MatIcon, MatIconButton, TranslatePipe, LoadingBarComponent],
  standalone: true
})
export class EventEditDialogComponent implements AddressReadAPI, AudienceReadAPI, CategoryReadAPI, EventReadAPI {
  private eventData = inject<{ id: number }>(MAT_DIALOG_DATA)
  readonly dialogRef = inject(MatDialogRef<EventEditDialogComponent>)
  private service = inject(EventService)
  private addressService = inject(AddressService)
  private categoryService = inject(CategoryService)
  private audienceService = inject(AudienceService)
  private translateService = inject(TranslateService)
  private toastService = inject(HotToastService)
  private bottomSheet = inject(MatBottomSheet)

  private infoResource = resource({
    loader: (p) => toPromise(this.service.getEventInfo(this.eventData.id), p.abortSignal)
  })

  readonly event = computed(() => this.infoResource.value()?.event)
  readonly reloading = this.infoResource.isLoading
  private participantCount = computed(() => this.infoResource.value()?.registration?.participants?.length ?? 0)

  getAllAddresses(page: number, size: number): Observable<Page<Address>> { return this.addressService.getAddresses(page, size) }
  getAddress(id: number): Observable<Address> { return this.addressService.getAddress(id) }
  createAddress(request: AddressChangeRequest): Observable<Address> { return this.addressService.createAddress(request) }
  getAllCategories(page: number, size: number): Observable<Page<Category>> { return this.categoryService.getCategories(page, size) }
  getCategory(id: number): Observable<Category> { return this.categoryService.getCategory(id) }
  getAllAudiences(page: number, size: number): Observable<Page<Audience>> { return this.audienceService.getAudiences(page, size) }
  getAudience(id: number): Observable<Audience> { return this.audienceService.getAudience(id) }
  getEvent(id: number): Observable<Event> { return this.service.getEvent(id) }
  getEventInfo(id: number): Observable<EventInfo> { return this.service.getEventInfo(id) }
  create(request: EventChangeRequest): Observable<Event> { return this.service.create(request) }

  handleRequest(request: EventChangeRequest) {
    const event = this.event()
    if (!event) return
    this.service.update(event.id, request).subscribe({
      next: (value) => {
        this.translateService.get('event.message.update.succeed').subscribe((msg) => {
          this.toastService.success(msg)
          if (this.participantCount() > 0) {
            this.translateService.get('event.notify.defaultSubject', { event: value.title }).subscribe((subject) => {
              this.bottomSheet.open(EventBroadcastSheetComponent, {
                data: { eventId: value.id, eventTitle: value.title, participantCount: this.participantCount(), defaultSubject: subject }
              })
            })
          }
          this.dialogRef.close(true)
        })
      },
      error: () => this.translateService.get('event.message.update.failed').subscribe((msg) => this.toastService.error(msg))
    })
  }
}
