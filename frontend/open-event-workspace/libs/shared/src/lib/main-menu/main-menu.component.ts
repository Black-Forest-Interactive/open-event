import {Component, input, output} from '@angular/core';
import {MainNavItem} from "../../../../../apps/open-event-app/src/shared/dashboard/main-nav-item";
import {RouterLink, RouterLinkActive} from "@angular/router";
import {MatIcon} from "@angular/material/icon";
import {TranslatePipe} from "@ngx-translate/core";

@Component({
  selector: 'app-main-menu',
  templateUrl: './main-menu.component.html',
  styleUrl: './main-menu.component.scss',
  imports: [
    RouterLink,
    RouterLinkActive,
    MatIcon,
    TranslatePipe
  ],
  standalone: true
})
export class MainMenuComponent {

  accessibleItems = input<MainNavItem[]>([])
  logoutEvent = output()

  constructor() {
  }

  logout() {
    this.logoutEvent.emit()
  }
}
