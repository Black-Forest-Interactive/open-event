import {Component, EventEmitter, Input, Output} from '@angular/core';
import {AppService} from "../../app.service";

@Component({
    selector: 'app-dashboard-toolbar',
    templateUrl: './dashboard-toolbar.component.html',
    styleUrl: './dashboard-toolbar.component.scss',
    standalone: false
})
export class DashboardToolbarComponent {
  @Input() mobileView: boolean = false
  @Input() title: string = ""
  @Output() toggleSidenavEvent = new EventEmitter<boolean>()

  constructor(
    public service: AppService
  ) {
  }

  logout() {
    this.service.logout()
  }
}
