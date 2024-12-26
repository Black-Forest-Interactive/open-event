import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {AccountService} from "../../account/model/account.service";
import {Account} from "../../account/model/account-api";

@Component({
    selector: 'app-event-create-dialog',
    templateUrl: './event-create-dialog.component.html',
    styleUrl: './event-create-dialog.component.scss',
    standalone: false
})
export class EventCreateDialogComponent {

  account: Account | undefined
  loading: boolean = false

  constructor(
    public dialogRef: MatDialogRef<EventCreateDialogComponent>,
    private accountService: AccountService,
    @Inject(MAT_DIALOG_DATA) public data: number | undefined,
  ) {
    if (data) {
      this.loading = true
      this.accountService.getAccount(data).subscribe({
        next: value => {
          this.account = value
          this.loading = false
        }
      })
    }
  }
}
