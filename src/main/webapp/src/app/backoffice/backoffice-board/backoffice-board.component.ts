import {Component} from '@angular/core';
import {AuthService} from "../../auth/auth.service";

@Component({
  selector: 'app-backoffice-board',
  templateUrl: './backoffice-board.component.html',
  styleUrls: ['./backoffice-board.component.scss']
})
export class BackofficeBoardComponent {

  canExport: boolean = false
  canIndexSearch: boolean = false
  canAdminAccount: boolean = false
  canCreateEvents: boolean = false

  reloading: boolean = false

  constructor(
    private authService: AuthService,
  ) {
  }

  ngOnInit() {
    this.canExport = this.authService.hasRole(AuthService.PERMISSION_EXPORT)
    this.canIndexSearch = this.authService.hasRole(AuthService.CATEGORY_ADMIN, AuthService.ACCOUNT_ADMIN, AuthService.EVENT_WRITE)
    this.canAdminAccount = this.authService.hasRole(AuthService.ACCOUNT_ADMIN)
    this.canCreateEvents = this.authService.hasRole(AuthService.EVENT_ADMIN)
  }


}
