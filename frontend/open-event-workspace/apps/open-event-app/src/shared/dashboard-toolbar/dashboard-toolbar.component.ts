import {Component, input, output} from '@angular/core';
import {AppService} from "../app.service";
import {MatToolbar} from "@angular/material/toolbar";
import {MatIcon} from "@angular/material/icon";
import {MatIconButton} from "@angular/material/button";
import {TranslatePipe} from "@ngx-translate/core";
import {MatMenu, MatMenuItem, MatMenuTrigger} from "@angular/material/menu";
import {AccountDisplayNamePipe} from "@open-event-workspace/core";
import {GravatarModule} from "ngx-gravatar";
import {MatDivider} from "@angular/material/divider";
import {RouterLink} from "@angular/router";
import {ActivityButtonComponent} from "../../core/activity/activity-button/activity-button.component";

@Component({
  selector: 'app-dashboard-toolbar',
  templateUrl: './dashboard-toolbar.component.html',
  styleUrl: './dashboard-toolbar.component.scss',
  imports: [
    MatToolbar,
    MatIcon,
    MatIconButton,
    TranslatePipe,
    MatMenuTrigger,
    AccountDisplayNamePipe,
    GravatarModule,
    MatMenu,
    MatDivider,
    MatMenuItem,
    RouterLink,
    ActivityButtonComponent
  ],
  standalone: true
})
export class DashboardToolbarComponent {
  mobileView = input<boolean>(false)
  title = input<string>("")
  toggleSidenavEvent = output<boolean>()

  constructor(
    public service: AppService
  ) {
  }

  logout() {
    this.service.logout()
  }
}
