import {Component, computed, EventEmitter, Input, Output, Signal} from '@angular/core';
import {Location, NgForOf} from "@angular/common";
import {EventMenuComponent} from "../event-menu/event-menu.component";
import {Router} from "@angular/router";
import {HotToastService} from "@ngxpert/hot-toast";
import {MatDialog} from "@angular/material/dialog";
import {Event, EventInfo} from "@open-event-workspace/core";
import {MatToolbar} from "@angular/material/toolbar";
import {MatMiniFabButton} from "@angular/material/button";
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {MatMenu, MatMenuItem, MatMenuTrigger} from "@angular/material/menu";
import {MatIcon} from "@angular/material/icon";
import {TranslatePipe} from "@ngx-translate/core";
import {EventService} from "@open-event-workspace/app";
import {BreakpointObserver, Breakpoints, BreakpointState} from "@angular/cdk/layout";
import {toSignal} from "@angular/core/rxjs-interop";

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
  isOwner: boolean = false
  @Output() changed: EventEmitter<Event> = new EventEmitter();
  menu: EventMenuComponent

  private mobileBreakpoint: Signal<BreakpointState | undefined>
  isMobile = computed(() => this.mobileBreakpoint()?.matches ?? false)


  constructor(
    router: Router,
    private location: Location,
    service: EventService,
    toastService: HotToastService,
    dialog: MatDialog,
    private breakpointObserver: BreakpointObserver
  ) {
    this.menu = new EventMenuComponent(router, dialog, service, toastService)
    this.mobileBreakpoint = toSignal(this.breakpointObserver.observe([Breakpoints.XSmall, Breakpoints.Small]))
  }


  @Input()
  set data(value: EventInfo | undefined) {
    this.info = value
    if (this.info && this.info.canEdit) this.isOwner = true
    if (value) this.menu.data = value.event
  }

  ngOnInit() {
    this.menu.changed.subscribe(e => this.changed.emit(e))
  }

  back() {
    this.location.back()
  }

}
