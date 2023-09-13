import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Event, EventInfo} from "../model/event-api";
import {Location} from "@angular/common";
import {EventMenuComponent} from "../event-menu/event-menu.component";
import {Router} from "@angular/router";
import {EventService} from "../model/event.service";
import {HotToastService} from "@ngneat/hot-toast";
import {MatDialog} from "@angular/material/dialog";
import {AuthService} from "../../auth/auth.service";
import {ExportService} from "../../backoffice/model/export.service";

@Component({
  selector: 'app-event-details-header',
  templateUrl: './event-details-header.component.html',
  styleUrls: ['./event-details-header.component.scss']
})
export class EventDetailsHeaderComponent {

  @Input()
  set data(value: EventInfo | undefined) {
    this.info = value
    if (this.info && this.info.canEdit) this.isAdminOrCanEdit = true
    if (value) this.menu.data = value.event
  }

  info: EventInfo | undefined
  @Input() reloading: boolean = false
  isAdminOrCanEdit: boolean = false
  @Output() changed: EventEmitter<Event> = new EventEmitter();
  menu: EventMenuComponent

  constructor(
    router: Router,
    private location: Location,
    service: EventService,
    exportService: ExportService,
    toastService: HotToastService,
    dialog: MatDialog,
    private authService: AuthService
  ) {
    this.menu = new EventMenuComponent(router, dialog, service, exportService, toastService)
  }

  ngOnInit() {
    if (this.authService.hasRole(AuthService.EVENT_ADMIN)) this.isAdminOrCanEdit = true
    this.menu.changed.subscribe(e => this.changed.emit(e))
  }

  back() {
    this.location.back()
  }

}
