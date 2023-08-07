import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Event} from "../model/event-api";
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {EventService} from "../model/event.service";
import {EventNavigationService} from "../event-navigation.service";
import {EventDeleteDialogComponent} from "../event-delete-dialog/event-delete-dialog.component";
import {EventMenuItem} from "../model/event-menu-item";
import {HotToastService} from "@ngneat/hot-toast";

@Component({
  selector: 'app-event-menu',
  templateUrl: './event-menu.component.html',
  styleUrls: ['./event-menu.component.scss']
})
export class EventMenuComponent {
  @Input()
  set data(value: Event) {
    this.event = value;
    this.publishMenuItem.disabled = value.published;
  }

  @Output() changed: EventEmitter<Event> = new EventEmitter();

  event: Event | undefined

  publishing: boolean = false

  editMenuItem = new EventMenuItem('edit', 'event.action.Edit', this.handleActionEdit.bind(this), false)
  copyMenuItem = new EventMenuItem('content_copy', 'event.action.Copy', this.handleActionEdit.bind(this), false)
  deleteMenuItem = new EventMenuItem('delete', 'event.action.Delete', this.handleActionCopy.bind(this), false)
  publishMenuItem = new EventMenuItem('publish', 'event.action.Publish', this.handleActionPublish.bind(this), false)

  menuItems = [
    this.editMenuItem,
    this.copyMenuItem,
    this.deleteMenuItem
  ]

  constructor(
    private router: Router,
    public dialog: MatDialog,
    private service: EventService,
    private toastService: HotToastService
  ) {
  }

  private handleActionEdit() {
    if (this.event) EventNavigationService.navigateToEventEdit(this.router, this.event.id)
  }

  private handleActionCopy() {
    if (this.event) EventNavigationService.navigateToEventCopy(this.router, this.event.id)
  }

  private handleActionShowDetails() {
    if (this.event) EventNavigationService.navigateToEventDetails(this.router, this.event.id)
  }

  private handleActionDelete() {
    if (!this.event) return
    const dialogRef = this.dialog.open(EventDeleteDialogComponent, {
      width: '250px',
      data: this.event
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result && this.event) this.service.deleteEvent(this.event.id).subscribe(d => EventNavigationService.navigateToEventShow(this.router))
    });
  }

  private handleActionPublish() {
    if (!this.event) return
    if (!this.event) return
    if (this.publishing) return
    this.publishing = true
    this.service.publish(this.event.id).subscribe({
      next: d => {
        this.changed.emit(d)
        this.publishing = false
      },
      error: err => {
        this.toastService.error(err)
        this.publishing = false
      }
    })
  }


}
