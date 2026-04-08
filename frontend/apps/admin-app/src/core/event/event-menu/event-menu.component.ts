import { AfterViewInit, Component, inject, input, output, viewChild } from '@angular/core'
import { EventInfo } from '@open-event/core'
import { EventMenuItem } from './event-menu-item'
import { EventService } from '@open-event/admin'
import { MatMenu, MatMenuItem, MatMenuTrigger } from '@angular/material/menu'
import { TranslatePipe } from '@ngx-translate/core'
import { MatIcon } from '@angular/material/icon'
import { MatDialog } from '@angular/material/dialog'
import { EventDeleteDialogComponent } from '../event-delete-dialog/event-delete-dialog.component'
import { Router } from '@angular/router'
import { EventChangeDialogComponent } from '../event-change-dialog/event-change-dialog.component'

@Component({
  selector: 'admin-event-menu',
  imports: [MatIcon, MatMenu, TranslatePipe, MatIcon, MatMenuItem],
  templateUrl: './event-menu.component.html',
  styleUrl: './event-menu.component.scss',
  exportAs: 'matMenu'
})
export class EventMenuComponent implements AfterViewInit {
  event = input<EventInfo>()
  menu = viewChild.required<MatMenu>('menu')
  reload = output()
  editMenuItem = new EventMenuItem('edit', 'event.action.edit', this.handleActionEdit.bind(this), false)
  // copyMenuItem = new EventMenuItem('content_copy', 'event.action.copy', this.handleActionCopy.bind(this), false)
  deleteMenuItem = new EventMenuItem('delete', 'event.action.delete', this.handleActionDelete.bind(this), false)
  menuItems = [
    this.editMenuItem,
    // this.copyMenuItem,
    this.deleteMenuItem
    // this.adminMenuItem
  ]
  private service = inject(EventService)
  private dialog = inject(MatDialog)
  private router = inject(Router)
  private menuTrigger = new MatMenuTrigger()

  ngAfterViewInit() {
    this.menuTrigger.menu = this.menu()
  }

  trigger() {
    if (this.menuTrigger.menuOpen) return
    this.menuTrigger.openMenu()
  }

  private handleActionEdit() {
    if (!this.event()) return
    this.dialog
      .open(EventChangeDialogComponent, { data: this.event()?.event })
      .afterClosed()
      .subscribe((value) => {
        if (value) this.reload.emit()
      })
  }

  private handleActionCopy() {
    if (!this.event()) return
    // if (this.event) EventNavigationService.navigateToEventCopy(this.router, this.event.id)
  }

  private handleActionShowDetails() {
    // if (this.event) EventNavigationService.navigateToEventDetails(this.router, this.event.id)
  }

  private handleActionAdmin() {
    // if (this.event) EventNavigationService.navigateToEventAdministration(this.router, this.event.id)
  }

  private handleActionDelete() {
    if (!this.event()) return
    const dialogRef = this.dialog.open(EventDeleteDialogComponent, {
      width: '350px',
      data: this.event()
    })
    dialogRef.afterClosed().subscribe((result) => {
      const event = this.event()
      if (result && event) this.service.deleteEvent(event.event.id).subscribe(() => this.router.navigate(['event']))
    })
  }
}
