import { Component, inject } from '@angular/core'
import { Address, AddressReadAPI, Category, CategoryReadAPI, Event, EventChangeComponent, EventChangeRequest, EventInfo, EventReadAPI } from '@open-event/core'
import { MatToolbar } from '@angular/material/toolbar'
import { Location } from '@angular/common'
import { TranslatePipe, TranslateService } from '@ngx-translate/core'
import { MatMiniFabButton } from '@angular/material/button'
import { MatIcon } from '@angular/material/icon'
import { AddressService, CategoryService, EventService } from '@open-event/portal'
import { Observable } from 'rxjs'
import { Page } from '@open-event/shared'
import { HotToastService } from '@ngxpert/hot-toast'
import { Router } from '@angular/router'
import { MatSlideToggle } from '@angular/material/slide-toggle'

@Component({
  selector: 'app-event-create',
  imports: [EventChangeComponent, MatIcon, MatMiniFabButton, MatToolbar, TranslatePipe, MatSlideToggle],
  templateUrl: './event-create.component.html',
  styleUrl: './event-create.component.scss'
})
export class EventCreateComponent implements AddressReadAPI, CategoryReadAPI, EventReadAPI {
  private service = inject(EventService)
  private addressService = inject(AddressService)
  private categoryService = inject(CategoryService)
  private translationService = inject(TranslateService)
  private toastService = inject(HotToastService)
  private router = inject(Router)
  private location = inject(Location)

  getAllAddresses(page: number, size: number): Observable<Page<Address>> {
    return this.addressService.getAddresses(page, size)
  }

  getAddress(id: number): Observable<Address> {
    return this.addressService.getAddress(id)
  }

  getAllCategories(page: number, size: number): Observable<Page<Category>> {
    return this.categoryService.getCategories(page, size)
  }

  getCategory(id: number): Observable<Category> {
    return this.categoryService.getCategory(id)
  }

  getEvent(id: number): Observable<Event> {
    return this.service.getEvent(id)
  }

  getEventInfo(id: number): Observable<EventInfo> {
    return this.service.getEventInfo(id)
  }

  cancel() {
    this.location.back()
  }

  handleRequest(request: EventChangeRequest) {
    this.service.create(request).subscribe({
      next: (event) => {
        this.translationService.get('event.message.create.succeed').subscribe((msg) => {
          this.toastService.success(msg)
          this.router.navigate(['/event/details/' + event.id]).then()
        })
      },
      error: (err) => this.translationService.get('event.message.create.failed').subscribe((msg) => this.toastService.error())
    })
  }

  private handleCreated(event: Event) {}
}
