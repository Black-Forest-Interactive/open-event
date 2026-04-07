import { Component, computed, effect, inject, input, output, signal } from '@angular/core'
import { Location } from '@angular/common'
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
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout'
import { toSignal } from '@angular/core/rxjs-interop'
import { EventDeleteDialogComponent } from '../event-delete-dialog/event-delete-dialog.component'
import { EventMenuItem } from '../event-menu-item'
import { EventNavigationService } from '../event-navigation.service'

@Component({
  selector: 'portal-event-details-header',
  templateUrl: './event-details-header.component.html',
  styleUrls: ['./event-details-header.component.scss'],
  imports: [MatToolbar, MatMiniFabButton, MatProgressSpinner, MatMenuTrigger, MatMenu, MatIcon, TranslatePipe, MatMenuItem],
  standalone: true
})
export class EventDetailsHeaderComponent {
  private location = inject(Location)
  private router = inject(Router)
  private service = inject(EventService)
  private toastService = inject(HotToastService)
  private dialog = inject(MatDialog)
  private breakpointObserver = inject(BreakpointObserver)

  data = input<EventInfo | undefined>()
  reloading = input<boolean>(false)
  changed = output<Event>()

  readonly isOwner = signal(false)
  private event = signal<Event | undefined>(undefined)
  private mobileBreakpoint = toSignal(this.breakpointObserver.observe([Breakpoints.XSmall, Breakpoints.Small]))
  readonly isMobile = computed(() => this.mobileBreakpoint()?.matches ?? false)
  readonly publishing = signal(false)

  readonly editMenuItem = new EventMenuItem('edit', 'event.action.edit', () => this.handleActionEdit(), false)
  readonly copyMenuItem = new EventMenuItem('content_copy', 'event.action.copy', () => this.handleActionCopy(), false)
  readonly deleteMenuItem = new EventMenuItem('delete', 'event.action.delete', () => this.handleActionDelete(), false)
  readonly adminMenuItem = new EventMenuItem('admin_panel_settings', 'event.action.admin', () => this.handleActionAdmin(), false)
  readonly publishMenuItem = new EventMenuItem('publish', 'event.action.publish', () => this.handleActionPublish(), false)
  readonly menuItems = [this.editMenuItem, this.copyMenuItem, this.deleteMenuItem, this.adminMenuItem]

  constructor() {
    effect(() => {
      const value = this.data()
      if (value?.canEdit) this.isOwner.set(true)
      if (value) {
        this.event.set(value.event)
        this.publishMenuItem.disabled = value.event.published
      }
    })
  }

  back() { this.location.back() }

  private handleActionEdit() {
    const e = this.event()
    if (e) EventNavigationService.navigateToEventEdit(this.router, e.id)
  }

  private handleActionCopy() {
    const e = this.event()
    if (e) EventNavigationService.navigateToEventCopy(this.router, e.id)
  }

  private handleActionAdmin() {
    const e = this.event()
    if (e) EventNavigationService.navigateToEventAdministration(this.router, e.id)
  }

  private handleActionDelete() {
    const e = this.event()
    if (!e) return
    this.dialog.open(EventDeleteDialogComponent, { width: '350px', data: e }).afterClosed().subscribe((result) => {
      if (result) this.service.deleteEvent(e.id).subscribe(() => EventNavigationService.navigateToEventShow(this.router))
    })
  }

  private handleActionPublish() {
    const e = this.event()
    if (!e || this.publishing()) return
    this.publishing.set(true)
    this.service.publish(e.id).subscribe({
      next: (d) => { this.changed.emit(d); this.publishing.set(false) },
      error: () => { this.toastService.error(); this.publishing.set(false) }
    })
  }
}
