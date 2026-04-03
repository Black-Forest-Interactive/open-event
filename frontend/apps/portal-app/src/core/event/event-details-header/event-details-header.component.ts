import { Component, computed, effect, inject, input, OnInit, output, Signal } from '@angular/core'
import { Location } from '@angular/common'
import { EventMenuComponent } from '../event-menu/event-menu.component'
import { Router } from '@angular/router'
import { HotToastService } from '@ngxpert/hot-toast'
import { MatDialog } from '@angular/material/dialog'
import { Event, EventInfo } from '@open-event/core'
import { MatToolbar } from '@angular/material/toolbar'
import { MatMiniFabButton } from '@angular/material/button'
import { MatProgressSpinner } from '@angular/material/progress-spinner'
import { MatMenu, MatMenuItem, MatMenuTrigger } from '@angular/material/menu'
import { MatIcon } from '@angular/material/icon'
import { TranslatePipe } from '@ngx-translate/core'
import { EventService } from '@open-event/portal'
import { BreakpointObserver, Breakpoints, BreakpointState } from '@angular/cdk/layout'
import { toSignal } from '@angular/core/rxjs-interop'

@Component({
  selector: 'portal-event-details-header',
  templateUrl: './event-details-header.component.html',
  styleUrls: ['./event-details-header.component.scss'],
  imports: [MatToolbar, MatMiniFabButton, MatProgressSpinner, MatMenuTrigger, MatMenu, MatIcon, TranslatePipe, MatMenuItem],
  standalone: true
})
export class EventDetailsHeaderComponent implements OnInit {
  private location = inject(Location)
  private breakpointObserver = inject(BreakpointObserver)

  data = input<EventInfo | undefined>()
  reloading = input<boolean>(false)
  changed = output<Event>()

  info: EventInfo | undefined
  isOwner: boolean = false
  menu: EventMenuComponent

  private mobileBreakpoint: Signal<BreakpointState | undefined>
  readonly isMobile = computed(() => this.mobileBreakpoint()?.matches ?? false)

  constructor() {
    const router = inject(Router)
    const service = inject(EventService)
    const toastService = inject(HotToastService)
    const dialog = inject(MatDialog)

    this.menu = new EventMenuComponent(router, dialog, service, toastService)
    this.mobileBreakpoint = toSignal(this.breakpointObserver.observe([Breakpoints.XSmall, Breakpoints.Small]))

    effect(() => {
      const value = this.data()
      this.info = value
      if (this.info?.canEdit) this.isOwner = true
      if (value) this.menu.data = value.event
    })
  }

  ngOnInit() {
    this.menu.changed.subscribe((e) => this.changed.emit(e))
  }

  back() {
    this.location.back()
  }
}
