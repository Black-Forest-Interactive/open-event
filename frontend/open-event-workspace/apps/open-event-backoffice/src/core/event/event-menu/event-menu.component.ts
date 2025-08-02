import {AfterViewInit, Component, input, output, viewChild} from '@angular/core';
import {EventInfo} from "@open-event-workspace/core";
import {EventMenuItem} from './event-menu-item';
import {EventService} from "@open-event-workspace/backoffice";
import {MatMenu, MatMenuItem, MatMenuTrigger} from "@angular/material/menu";
import {TranslatePipe} from "@ngx-translate/core";
import {MatIcon} from "@angular/material/icon";
import {MatDialog} from "@angular/material/dialog";
import {EventDeleteDialogComponent} from "../event-delete-dialog/event-delete-dialog.component";
import {Router} from "@angular/router";
import {EventChangeDialogComponent} from "../event-change-dialog/event-change-dialog.component";

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
  reload = output()
  private menuTrigger = new MatMenuTrigger()

  editMenuItem = new EventMenuItem('edit', 'event.action.edit', this.handleActionEdit.bind(this), false)
  // copyMenuItem = new EventMenuItem('content_copy', 'event.action.copy', this.handleActionCopy.bind(this), false)
  deleteMenuItem = new EventMenuItem('delete', 'event.action.delete', this.handleActionDelete.bind(this), false)

  menuItems = [
    this.editMenuItem,
    // this.copyMenuItem,
    this.deleteMenuItem,
    // this.adminMenuItem
  ]

  constructor(private service: EventService, private dialog: MatDialog, private router: Router) {
  }

  ngAfterViewInit() {
    this.menuTrigger.menu = this.menu()
  }

  trigger() {
    if (this.menuTrigger.menuOpen) return
    this.menuTrigger.openMenu()
  }

  private handleActionEdit() {
    if (!this.event()) return
    this.dialog.open(EventChangeDialogComponent, {data: this.event()!!.event}).afterClosed().subscribe(value => {
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
    dialogRef.afterClosed().subscribe(result => {
      if (result && this.event()) this.service.deleteEvent(this.event()!!.event.id).subscribe(d => this.router.navigate(['event']))
    })
  }

}
