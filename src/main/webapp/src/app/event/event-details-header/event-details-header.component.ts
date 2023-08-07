import {Component, Input} from '@angular/core';
import {EventInfo} from "../model/event-api";
import {Location} from "@angular/common";
import {EventMenuComponent} from "../event-menu/event-menu.component";
import {Router} from "@angular/router";
import {EventService} from "../model/event.service";
import {HotToastService} from "@ngneat/hot-toast";
import {MatDialog} from "@angular/material/dialog";

@Component({
  selector: 'app-event-details-header',
  templateUrl: './event-details-header.component.html',
  styleUrls: ['./event-details-header.component.scss']
})
export class EventDetailsHeaderComponent {

  @Input()
  set data(value: EventInfo | undefined) {
    this.info = value
    if (value) this.menu.data = value.event
  }

  info: EventInfo | undefined
  @Input() reloading: boolean = false

  menu: EventMenuComponent

  constructor(
    router: Router,
    private location: Location,
    service: EventService,
    toastService: HotToastService,
    dialog: MatDialog,
  ) {
    this.menu = new EventMenuComponent(router, dialog, service, toastService)
  }

  back() {
    this.location.back()
  }
}
