import { Component, computed, inject, resource, signal } from '@angular/core'
import { Address, AddressChangeRequest, AddressReadAPI, Audience, AudienceReadAPI, Category, CategoryReadAPI, Event, EventChangeRequest, EventInfo, EventReadAPI } from '@open-event/core'
import { EventChangeComponent } from '@open-event/ui'
import { MatToolbar } from '@angular/material/toolbar'
import { MatBottomSheet } from '@angular/material/bottom-sheet'
import { TranslatePipe, TranslateService } from '@ngx-translate/core'
import { MatIcon } from '@angular/material/icon'
import { MatIconButton } from '@angular/material/button'
import { MatTooltip } from '@angular/material/tooltip'
import { MatCard } from '@angular/material/card'
import { AddressService, AudienceService, CategoryService, EventService } from '@open-event/portal'
import { HotToastService } from '@ngxpert/hot-toast'
import { ActivatedRoute, Router } from '@angular/router'
import { Location } from '@angular/common'
import { Observable } from 'rxjs'
import { map } from 'rxjs/operators'
import { LoadingBarComponent, Page, toPromise } from '@open-event/shared'
import { toSignal } from '@angular/core/rxjs-interop'
import { EventBroadcastSheetComponent } from '../../announcement/event-broadcast-sheet/event-broadcast-sheet.component'

@Component({
  selector: 'portal-event-edit',
  imports: [EventChangeComponent, MatIcon, MatIconButton, MatTooltip, MatToolbar, TranslatePipe, LoadingBarComponent, MatCard],
  templateUrl: './event-edit.component.html',
  styleUrl: './event-edit.component.scss'
})
export class EventEditComponent implements AddressReadAPI, AudienceReadAPI, CategoryReadAPI, EventReadAPI {
  private service = inject(EventService)
  private addressService = inject(AddressService)
  private categoryService = inject(CategoryService)
  private audienceService = inject(AudienceService)
  private translationService = inject(TranslateService)
  private toastService = inject(HotToastService)
  private router = inject(Router)
  private route = inject(ActivatedRoute)
  private location = inject(Location)
  private bottomSheet = inject(MatBottomSheet)

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

  readonly event = computed(() => this.infoResource.value()?.event)
  readonly reloading = this.infoResource.isLoading
  private participantCount = computed(() => this.infoResource.value()?.registration?.participants?.length ?? 0)

  getAllAddresses(page: number, size: number): Observable<Page<Address>> {
    return this.addressService.getAddresses(page, size)
  }
  getAddress(id: number): Observable<Address> {
    return this.addressService.getAddress(id)
  }
  createAddress(request: AddressChangeRequest): Observable<Address> {
    return this.addressService.createAddress(request)
  }
  getAllCategories(page: number, size: number): Observable<Page<Category>> {
    return this.categoryService.getCategories(page, size)
  }
  getCategory(id: number): Observable<Category> {
    return this.categoryService.getCategory(id)
  }
  getAllAudiences(page: number, size: number): Observable<Page<Audience>> {
    return this.audienceService.getAudiences(page, size)
  }
  getAudience(id: number): Observable<Audience> {
    return this.audienceService.getAudience(id)
  }
  getEvent(id: number): Observable<Event> {
    return this.service.getEvent(id)
  }

  getEventInfo(id: number): Observable<EventInfo> {
    return this.service.getEventInfo(id)
  }

  create(request: EventChangeRequest): Observable<Event> {
    return this.service.create(request)
  }

  cancel() {
    this.location.back()
  }

  handleRequest(request: EventChangeRequest) {
    const event = this.event()
    if (!event) return
    this.service.update(event.id, request).subscribe({
      next: (value) => {
        this.translationService.get('event.message.update.succeed').subscribe((msg) => {
          this.toastService.success(msg)
          if (this.participantCount() > 0) {
            this.translationService.get('event.notify.defaultSubject', { event: value.title }).subscribe((subject) => {
              this.bottomSheet.open(EventBroadcastSheetComponent, { data: { eventId: value.id, eventTitle: value.title, participantCount: this.participantCount(), defaultSubject: subject } })
            })
          }
          this.router.navigate(['/event/details/' + value.id]).then()
        })
      },
      error: () => this.translationService.get('event.message.update.failed').subscribe((msg) => this.toastService.error(msg))
    })
  }
}
