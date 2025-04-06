import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Location, NgForOf} from "@angular/common";
import {EventMenuComponent} from "../event-menu/event-menu.component";
import {Router} from "@angular/router";
import {HotToastService} from "@ngxpert/hot-toast";
import {MatDialog} from "@angular/material/dialog";
import {AuthService} from "../../../../../../libs/shared/src/lib/auth/auth.service";
import {Event, EventInfo, EventService} from "@open-event-workspace/core";
import {MatToolbar} from "@angular/material/toolbar";
import {MatMiniFabButton} from "@angular/material/button";
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {MatMenu, MatMenuItem, MatMenuTrigger} from "@angular/material/menu";
import {MatIcon} from "@angular/material/icon";
import {TranslatePipe} from "@ngx-translate/core";
import {Roles} from "../../../shared/roles";

@Component({
  selector: 'app-event-details-header',
  templateUrl: './event-details-header.component.html',
  styleUrls: ['./event-details-header.component.scss'],
  imports: [
    MatToolbar,
    MatMiniFabButton,
    MatProgressSpinner,
    MatMenuTrigger,
    MatMenu,
    MatIcon,
    TranslatePipe,
    NgForOf,
    MatMenuItem
  ],
  standalone: true
})
export class EventDetailsHeaderComponent {

  info: EventInfo | undefined
  @Input() reloading: boolean = false
  isAdminOrCanEdit: boolean = false
  @Output() changed: EventEmitter<Event> = new EventEmitter();
  menu: EventMenuComponent

  constructor(
    router: Router,
    private location: Location,
    service: EventService,
    toastService: HotToastService,
    dialog: MatDialog,
    private authService: AuthService
  ) {
    this.menu = new EventMenuComponent(router, dialog, service, toastService)
  }

  @Input()
  set data(value: EventInfo | undefined) {
    this.info = value
    if (this.info && this.info.canEdit) this.isAdminOrCanEdit = true
    if (value) this.menu.data = value.event
  }

  ngOnInit() {
    if (this.authService.hasRole(Roles.EVENT_ADMIN)) this.isAdminOrCanEdit = true
    if (this.authService.hasRole(Roles.EVENT_MODERATOR)) this.isAdminOrCanEdit = true
    this.menu.changed.subscribe(e => this.changed.emit(e))
  }

  back() {
    this.location.back()
  }

}
