import { Component, inject, input, output } from '@angular/core'
import { MatToolbar } from '@angular/material/toolbar'
import { Location } from '@angular/common'
import { TranslatePipe, TranslateService } from '@ngx-translate/core'
import { MatMiniFabButton } from '@angular/material/button'
import { MatIcon } from '@angular/material/icon'
import { MatCard } from '@angular/material/card'
import { Observable } from 'rxjs'
import { Address, AddressReadAPI, Audience, AudienceReadAPI, Category, CategoryReadAPI, Event, EventChangeRequest, EventInfo, EventReadAPI } from '@open-event/core'
import { Page } from '@open-event/shared'
import { HotToastService } from '@ngxpert/hot-toast'
import { EventChangeComponent } from '../event-change/event-change.component'

@Component({
  selector: 'lib-event-create',
  imports: [EventChangeComponent, MatIcon, MatMiniFabButton, MatToolbar, TranslatePipe, MatCard],
  templateUrl: './event-create.component.html',
  styleUrl: './event-create.component.scss'
})
export class EventCreateComponent implements AddressReadAPI, AudienceReadAPI, CategoryReadAPI, EventReadAPI {
  addressReadAPI = input.required<AddressReadAPI>()
  categoryReadAPI = input.required<CategoryReadAPI>()
  audienceReadAPI = input.required<AudienceReadAPI>()
  eventReadAPI = input.required<EventReadAPI>()

  created = output<Event>()
  cancelled = output<void>()

  private translationService = inject(TranslateService)
  private toastService = inject(HotToastService)
  private location = inject(Location)

  getAllAddresses(page: number, size: number): Observable<Page<Address>> {
    return this.addressReadAPI().getAllAddresses(page, size)
  }

  getAddress(id: number): Observable<Address> {
    return this.addressReadAPI().getAddress(id)
  }

  getAllCategories(page: number, size: number): Observable<Page<Category>> {
    return this.categoryReadAPI().getAllCategories(page, size)
  }

  getCategory(id: number): Observable<Category> {
    return this.categoryReadAPI().getCategory(id)
  }

  getAllAudiences(page: number, size: number): Observable<Page<Audience>> {
    return this.audienceReadAPI().getAllAudiences(page, size)
  }

  getAudience(id: number): Observable<Audience> {
    return this.audienceReadAPI().getAudience(id)
  }

  getEvent(id: number): Observable<Event> {
    return this.eventReadAPI().getEvent(id)
  }

  getEventInfo(id: number): Observable<EventInfo> {
    return this.eventReadAPI().getEventInfo(id)
  }

  create(request: EventChangeRequest): Observable<Event> {
    const create = this.eventReadAPI().create
    if (!create) throw new Error('create is not implemented')
    return (create as (r: EventChangeRequest) => Observable<Event>)(request)
  }

  cancel() {
    this.cancelled.emit()
    this.location.back()
  }

  handleRequest(request: EventChangeRequest) {
    this.eventReadAPI()
      .create(request)
      .subscribe({
        next: (event) => {
          this.translationService.get('event.message.create.succeed').subscribe((msg) => {
            this.toastService.success(msg)
            this.created.emit(event)
          })
        },
        error: () => this.translationService.get('event.message.create.failed').subscribe((msg) => this.toastService.error(msg))
      })
  }
}
