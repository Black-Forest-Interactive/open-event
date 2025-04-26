import {AfterViewInit, Component, input, viewChild} from '@angular/core';
import {EventInfo} from "@open-event-workspace/core";
import {EventMenuItem} from './event-menu-item';
import {EventService} from "@open-event-workspace/backoffice";
import {MatMenu, MatMenuItem, MatMenuTrigger} from "@angular/material/menu";
import {TranslatePipe} from "@ngx-translate/core";
import {MatIcon} from "@angular/material/icon";

@Component({
  selector: 'app-event-menu',
  imports: [
    MatIcon,
    MatMenu,
    TranslatePipe,
    MatIcon,
    MatMenuItem
  ],
  templateUrl: './event-menu.component.html',
  styleUrl: './event-menu.component.scss',
  exportAs: 'matMenu'
})
export class EventMenuComponent implements AfterViewInit {

  event = input<EventInfo>()
  menu = viewChild.required<MatMenu>('menu')
  private menuTrigger = new MatMenuTrigger()

  editMenuItem = new EventMenuItem('edit', 'event.action.edit', this.handleActionEdit.bind(this), false)
  copyMenuItem = new EventMenuItem('content_copy', 'event.action.copy', this.handleActionCopy.bind(this), false)
  deleteMenuItem = new EventMenuItem('delete', 'event.action.delete', this.handleActionDelete.bind(this), false)
  // adminMenuItem = new EventMenuItem('admin_panel_settings', 'event.action.admin', this.handleActionAdmin.bind(this), false)
  publishMenuItem = new EventMenuItem('publish', 'event.action.publish', this.handleActionPublish.bind(this), false)
  menuItems = [
    this.editMenuItem,
    this.copyMenuItem,
    this.deleteMenuItem,
    // this.adminMenuItem
  ]

  constructor(private service: EventService) {
  }

  ngAfterViewInit() {
    this.menuTrigger.menu = this.menu()
  }

  trigger() {
    if (this.menuTrigger.menuOpen) return
    this.menuTrigger.openMenu()
  }

  private handleActionEdit() {
    // if (this.event) EventNavigationService.navigateToEventEdit(this.router, this.event.id)
  }

  private handleActionCopy() {
    // if (this.event) EventNavigationService.navigateToEventCopy(this.router, this.event.id)
  }

  private handleActionShowDetails() {
    // if (this.event) EventNavigationService.navigateToEventDetails(this.router, this.event.id)
  }

  private handleActionAdmin() {
    // if (this.event) EventNavigationService.navigateToEventAdministration(this.router, this.event.id)
  }

  private handleActionDelete() {
    // if (!this.event) return
    // const dialogRef = this.dialog.open(EventDeleteDialogComponent, {
    //   width: '350px',
    //   data: this.event
    // });
    // dialogRef.afterClosed().subscribe(result => {
    //   if (result && this.event) this.service.deleteEvent(this.event.id).subscribe(d => EventNavigationService.navigateToEventShow(this.router))
    // });
  }

  private handleActionPublish() {
    if (!this.event) return
    // if (this.publishing) return
    // this.publishing = true
    // this.service.publish(this.event.id).subscribe({
    //   next: d => {
    //     this.changed.emit(d)
    //     this.publishing = false
    //   },
    //   error: err => {
    //     this.toastService.error(err)
    //     this.publishing = false
    //   }
    // })
  }
}
