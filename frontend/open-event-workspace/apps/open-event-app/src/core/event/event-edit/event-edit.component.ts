import {Component, OnInit} from '@angular/core';
import {Address, AddressReadAPI, Category, CategoryReadAPI, Event, EventChangeComponent, EventChangeRequest, EventInfo, EventReadAPI} from "@open-event-workspace/core";
import {MatToolbar} from "@angular/material/toolbar";
import {TranslatePipe, TranslateService} from "@ngx-translate/core";
import {MatIcon} from "@angular/material/icon";
import {MatMiniFabButton} from "@angular/material/button";
import {AddressService, CategoryService, EventService} from "@open-event-workspace/app";
import {HotToastService} from "@ngxpert/hot-toast";
import {ActivatedRoute, ParamMap, Router} from "@angular/router";
import {Location} from "@angular/common";
import {Observable} from "rxjs";
import {LoadingBarComponent, Page} from "@open-event-workspace/shared";

@Component({
  selector: 'app-event-edit',
  imports: [
    EventChangeComponent,
    MatIcon,
    MatMiniFabButton,
    MatToolbar,
    TranslatePipe,
    LoadingBarComponent
  ],
  templateUrl: './event-edit.component.html',
  styleUrl: './event-edit.component.scss'
})
export class EventEditComponent implements AddressReadAPI, CategoryReadAPI, EventReadAPI, OnInit {

  reloading: boolean = false
  event: Event | undefined

  constructor(
    private service: EventService,
    private addressService: AddressService,
    private categoryService: CategoryService,
    private translationService: TranslateService,
    private toastService: HotToastService,
    private router: Router,
    private route: ActivatedRoute,
    private location: Location
  ) {
  }


  ngOnInit() {
    this.route.paramMap.subscribe(p => this.handleParams(p))
  }


  private handleParams(p: ParamMap) {
    let idParam = p.get('id')
    let id = idParam !== null ? +idParam : null;
    if (id == null) return

    this.reloading = true
    this.service.getEvent(id).subscribe(data => this.handleData(data))
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
    this.service.update(this.event.id, request).subscribe({
      next: event => {
        this.translationService.get("event.message.update.succeed").subscribe(
          msg => {
            this.toastService.success(msg)
            this.router.navigate(["/event/details/" + event.id]).then()
          }
        )
      },
      error: err => this.translationService.get("event.message.update.failed").subscribe(
        msg => this.toastService.error(msg)
      )
    })
  }
}

