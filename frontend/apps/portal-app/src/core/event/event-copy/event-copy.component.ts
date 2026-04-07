import { Component, computed, inject, resource } from '@angular/core'
import { Address, AddressReadAPI, Category, CategoryReadAPI, Event, EventChangeComponent, EventChangeRequest, EventInfo, EventReadAPI } from '@open-event/core'
import { LoadingBarComponent, Page, toPromise } from '@open-event/shared'
import { MatToolbar } from '@angular/material/toolbar'
import { TranslatePipe, TranslateService } from '@ngx-translate/core'
import { AddressService, CategoryService, EventService } from '@open-event/portal'
import { HotToastService } from '@ngxpert/hot-toast'
import { ActivatedRoute, Router } from '@angular/router'
import { Location } from '@angular/common'
import { Observable } from 'rxjs'
import { map } from 'rxjs/operators'
import { MatIcon } from '@angular/material/icon'
import { MatIconButton } from '@angular/material/button'
import { MatTooltip } from '@angular/material/tooltip'
import { toSignal } from '@angular/core/rxjs-interop'

@Component({
  selector: 'portal-event-copy',
  imports: [EventChangeComponent, LoadingBarComponent, MatIcon, MatIconButton, MatTooltip, MatToolbar, TranslatePipe],
  templateUrl: './event-copy.component.html',
  styleUrl: './event-copy.component.scss'
})
export class EventCopyComponent implements AddressReadAPI, CategoryReadAPI, EventReadAPI {
  private service = inject(EventService)
  private addressService = inject(AddressService)
  private categoryService = inject(CategoryService)
  private translationService = inject(TranslateService)
  private toastService = inject(HotToastService)
  private router = inject(Router)
  private route = inject(ActivatedRoute)
  private location = inject(Location)

  private eventId = toSignal(this.route.paramMap.pipe(map(p => { const id = p.get('id'); return id ? +id : undefined })))

  private eventResource = resource({
    params: this.eventId,
    loader: (p) => p.params ? toPromise(this.service.getEvent(p.params), p.abortSignal) : Promise.resolve(undefined)
  })

  readonly event = computed(() => this.eventResource.value())
  readonly reloading = this.eventResource.isLoading

  getAllAddresses(page: number, size: number): Observable<Page<Address>> { return this.addressService.getAddresses(page, size) }
  getAddress(id: number): Observable<Address> { return this.addressService.getAddress(id) }
  getAllCategories(page: number, size: number): Observable<Page<Category>> { return this.categoryService.getCategories(page, size) }
  getCategory(id: number): Observable<Category> { return this.categoryService.getCategory(id) }
  getEvent(id: number): Observable<Event> { return this.service.getEvent(id) }
  getEventInfo(id: number): Observable<EventInfo> { return this.service.getEventInfo(id) }

  cancel() { this.location.back() }

  handleRequest(request: EventChangeRequest) {
    if (!this.event()) return
    this.service.create(request).subscribe({
      next: (event) => {
        this.translationService.get('event.message.copy.succeed').subscribe((msg) => {
          this.toastService.success(msg)
          this.router.navigate(['/event/details/' + event.id]).then()
        })
      },
      error: () => this.translationService.get('event.message.copy.failed').subscribe((msg) => this.toastService.error(msg))
    })
  }
}
