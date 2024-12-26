import {Component, Input} from '@angular/core';
import {MainNavItem} from "../dashboard/main-nav-item";
import {AuthService} from "../../auth/auth.service";
import {ConfirmLogoutDialogComponent} from "../confirm-logout-dialog/confirm-logout-dialog.component";
import {MatDialog} from "@angular/material/dialog";

@Component({
    selector: 'app-main-menu',
    templateUrl: './main-menu.component.html',
    styleUrl: './main-menu.component.scss',
    standalone: false
})
export class MainMenuComponent {

  @Input() accessibleItems: MainNavItem[] = []

  constructor(public authService: AuthService, private dialog: MatDialog,) {
  }

  logout() {
    const dialogRef = this.dialog.open(ConfirmLogoutDialogComponent, {
      width: '250px',
      data: ''
    })
    dialogRef.afterClosed().subscribe(result => {
      if (result) this.authService.logout()
    })
  }
}
