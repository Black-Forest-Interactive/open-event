import { Component, inject, OnInit } from '@angular/core'
import { Address, AddressReadAPI, Category, CategoryReadAPI, Event, EventChangeComponent, EventChangeRequest, EventInfo, EventReadAPI } from '@open-event/core'
import { LoadingBarComponent, Page } from '@open-event/shared'
import { MatToolbar } from '@angular/material/toolbar'
import { TranslatePipe, TranslateService } from '@ngx-translate/core'
import { AddressService, CategoryService, EventService } from '@open-event/portal'
import { HotToastService } from '@ngxpert/hot-toast'
import { ActivatedRoute, ParamMap, Router } from '@angular/router'
import { Location } from '@angular/common'
import { Observable } from 'rxjs'
import { MatIcon } from '@angular/material/icon'
import { MatMiniFabButton } from '@angular/material/button'

@Component({
  selector: 'portal-event-copy',
  imports: [EventChangeComponent, LoadingBarComponent, MatIcon, MatMiniFabButton, MatToolbar, TranslatePipe],
  templateUrl: './event-copy.component.html',
  styleUrl: './event-copy.component.scss'
})
export class EventCopyComponent implements AddressReadAPI, CategoryReadAPI, EventReadAPI, OnInit {
  private service = inject(EventService)
  private addressService = inject(AddressService)
  private categoryService = inject(CategoryService)
  private translationService = inject(TranslateService)
  private toastService = inject(HotToastService)
  private router = inject(Router)
  private route = inject(ActivatedRoute)
  private location = inject(Location)

  reloading: boolean = false
  event: Event | undefined

  ngOnInit() {
    this.route.paramMap.subscribe((p) => this.handleParams(p))
  }

  private handleParams(p: ParamMap) {
    const idParam = p.get('id')
    const id = idParam !== null ? +idParam : null
    if (id == null) return

    this.reloading = true
    this.service.getEvent(id).subscribe((data) => this.handleData(data))
  }

  private handleData(e: Event) {
    this.event = e
    this.reloading = false
  }

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
    if (!this.event) return
    this.service.create(request).subscribe({
      next: (event) => {
        this.translationService.get('event.message.copy.succeed').subscribe((msg) => {
          this.toastService.success(msg)
          this.router.navigate(['/event/details/' + event.id]).then()
        })
      },
      error: (err) => this.translationService.get('event.message.copy.failed').subscribe((msg) => this.toastService.error(msg))
    })
  }
}
