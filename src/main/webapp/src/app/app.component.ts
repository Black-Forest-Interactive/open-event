import {Component} from '@angular/core';
import {AccountService} from "./account/model/account.service";
import {Account, AccountValidationResult} from "./account/model/account-api";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {LoadingScreenComponent} from "./dashboard/loading-screen/loading-screen.component";
import {environment} from './../environments/environment';
import LogRocket from 'logrocket';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'open-event';

  validated: boolean = false
  account: Account | undefined
  dialogRef: MatDialogRef<any> | undefined

  constructor(private accountService: AccountService, private dialog: MatDialog) {

    if (environment.logrocket && environment.logrocketAppId.length > 0) {
      LogRocket.init(environment.logrocketAppId)
    }
  }

  ngOnInit() {
    this.dialogRef = this.dialog.open(LoadingScreenComponent, {disableClose: true})
    this.accountService.validate().subscribe(d => this.handleValidationResult(d))
  }

  private handleValidationResult(d: AccountValidationResult) {
    this.account = d.account
    this.validated = true
    if (environment.logrocket) {
      LogRocket.identify(d.account.id + '')
    }
    this.dialogRef?.close()
  }
}
