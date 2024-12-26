import {Injectable} from '@angular/core';
import {AccountService} from "./account/model/account.service";
import {TranslateService} from "@ngx-translate/core";
import {Account, AccountInfo, AccountValidationResult} from "./account/model/account-api";
import {Profile} from "./profile/model/profile-api";
import {Subject} from "rxjs";
import {ConfirmLogoutDialogComponent} from "./dashboard/confirm-logout-dialog/confirm-logout-dialog.component";
import {AuthService} from "./auth/auth.service";
import {MatDialog} from "@angular/material/dialog";

@Injectable({
  providedIn: 'root'
})
export class AppService {

  validated: Subject<boolean> = new Subject()
  account: Account | undefined
  profile: Profile | undefined
  info: AccountInfo | undefined


  constructor(
    private accountService: AccountService,
    private translate: TranslateService,
    public authService: AuthService,
    private dialog: MatDialog
  ) {
  }

  validate() {
    this.accountService.validate().subscribe(d => this.handleValidationResult(d))
  }

  private handleValidationResult(d: AccountValidationResult) {
    this.account = d.account
    this.profile = d.profile
    this.info = d.info
    this.translate.setDefaultLang('en')
    this.translate.use(d.profile.language)

    this.validated.next(true)
  }

  logout(){
    const dialogRef = this.dialog.open(ConfirmLogoutDialogComponent, {
      width: '250px',
      data: ''
    })
    dialogRef.afterClosed().subscribe(result => {
      if (result) this.authService.logout()
    })
  }
}
