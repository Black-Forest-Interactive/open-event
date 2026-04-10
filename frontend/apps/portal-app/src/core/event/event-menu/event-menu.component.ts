import { Component, inject, output, signal } from '@angular/core'
import { Router } from '@angular/router'
import { MatDialog } from '@angular/material/dialog'
import { EventDeleteDialogComponent } from '../event-delete-dialog/event-delete-dialog.component'
import { HotToastService } from '@ngxpert/hot-toast'
import { EventMenuItem } from '../event-menu-item'
import { Event } from '@open-event/core'
import { EventNavigationService } from '../event-navigation.service'
import { EventService } from '@open-event/portal'

@Component({
  selector: 'portal-event-menu',
  templateUrl: './event-menu.component.html',
  styleUrls: ['./event-menu.component.scss'],
  standalone: true
})
export class EventMenuComponent {
  changed = output<Event>()
  readonly publishing = signal(false)
  readonly editMenuItem = new EventMenuItem('edit', 'event.action.edit', () => this.handleActionEdit(), false)
  readonly copyMenuItem = new EventMenuItem('content_copy', 'event.action.copy', () => this.handleActionCopy(), false)
  readonly deleteMenuItem = new EventMenuItem('delete', 'event.action.delete', () => this.handleActionDelete(), false)
  readonly adminMenuItem = new EventMenuItem('admin_panel_settings', 'event.action.admin', () => this.handleActionAdmin(), false)
  readonly publishMenuItem = new EventMenuItem('publish', 'event.action.publish', () => this.handleActionPublish(), false)
  readonly menuItems = [this.editMenuItem, this.copyMenuItem, this.deleteMenuItem, this.adminMenuItem]
  private router = inject(Router)
  private dialog = inject(MatDialog)
  private service = inject(EventService)
  private toastService = inject(HotToastService)
  private event = signal<Event | undefined>(undefined)

  set data(value: Event) {
    this.event.set(value)
    this.publishMenuItem.disabled = value.published
  }

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
    this.dialog
      .open(EventDeleteDialogComponent, { width: '350px', data: e })
      .afterClosed()
      .subscribe((result) => {
        if (result) this.service.deleteEvent(e.id).subscribe(() => EventNavigationService.navigateToEventShow(this.router))
      })
  }

  private handleActionPublish() {
    const e = this.event()
    if (!e || this.publishing()) return
    this.publishing.set(true)
    this.service.publish(e.id).subscribe({
      next: (d) => {
        this.changed.emit(d)
        this.publishing.set(false)
      },
      error: () => {
        this.toastService.error()
        this.publishing.set(false)
      }
    })
  }
}
