import {Component} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {SearchAccountDialogComponent} from "../../account/search-account-dialog/search-account-dialog.component";
import {CreateAccountDialogComponent} from "../../account/create-account-dialog/create-account-dialog.component";
import {AccountService} from "../../account/model/account.service";
import {Account, AccountChangeRequest} from "../../account/model/account-api";
import {HotToastService} from "@ngxpert/hot-toast";
import {TranslateService} from "@ngx-translate/core";

@Component({
    selector: 'app-board-card-account',
    templateUrl: './board-card-account.component.html',
    styleUrls: ['./board-card-account.component.scss'],
    standalone: false
})
export class BoardCardAccountComponent {

  accountCreating: boolean = false

  constructor(
    private dialog: MatDialog,
    private accountService: AccountService,
    private translation: TranslateService,
    private toastService: HotToastService
  ) {
  }

  showSearchAccountDialog() {
    this.dialog.open(SearchAccountDialogComponent, {
      width: '800px',
    })
  }

  showCreateAccountDialog() {
    let dialogRef = this.dialog.open(CreateAccountDialogComponent)
    dialogRef.afterClosed().subscribe(request => {
      if (request) this.createAccount(request)
    })
  }

  private createAccount(request: AccountChangeRequest) {
    if (this.accountCreating) return
    this.accountCreating = true

    this.accountService.createAccount(request).subscribe(a => this.handleAccountCreationResult(a))
  }

  private handleAccountCreationResult(a: Account) {
    this.translation.get('account.message.created').subscribe(msg => this.toastService.success(msg))
    this.accountCreating = false
  }
}
