import {Component} from '@angular/core';
import {Address, AddressReadAPI, Category, CategoryReadAPI, Event, EventChangeComponent, EventChangeRequest, EventInfo, EventReadAPI} from "@open-event-workspace/core";
import {MatToolbar} from "@angular/material/toolbar";
import {Location} from "@angular/common";
import {TranslatePipe, TranslateService} from "@ngx-translate/core";
import {MatMiniFabButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {AddressService, CategoryService, EventService} from "@open-event-workspace/app";
import {Observable} from "rxjs";
import {Page} from "@open-event-workspace/shared";
import {HotToastService} from "@ngxpert/hot-toast";
import {Router} from "@angular/router";

@Component({
  selector: 'app-event-create',
  imports: [
    EventChangeComponent,
    MatIcon,
    MatMiniFabButton,
    MatToolbar,
    TranslatePipe
  ],
  templateUrl: './event-create.component.html',
  styleUrl: './event-create.component.scss'
})
export class EventCreateComponent implements AddressReadAPI, CategoryReadAPI, EventReadAPI {

  constructor(
    private service: EventService,
    private addressService: AddressService,
    private categoryService: CategoryService,
    private translationService: TranslateService,
    private toastService: HotToastService,
    private router: Router,
    private location: Location
  ) {
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
    this.service.create(request).subscribe({
      next: event => {
        this.translationService.get("event.message.create.succeed").subscribe(
          msg => {
            this.toastService.success(msg)
            this.router.navigate(["/event/details/" + event.id]).then()
          }
        )
      },
      error: err => this.translationService.get("event.message.create.failed").subscribe(
        msg => this.toastService.error(msg)
      )
    })
  }
}
