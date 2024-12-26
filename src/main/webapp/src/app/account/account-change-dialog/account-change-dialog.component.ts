import {Component, Inject} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {AccountService} from "../model/account.service";
import {AccountChangeRequest, AccountSetupRequest} from "../model/account-api";
import {ProfileChangeRequest} from "../../profile/model/profile-api";
import {AccountSearchEntry} from "../../search/model/search-api";

@Component({
    selector: 'app-account-change-dialog',
    templateUrl: './account-change-dialog.component.html',
    styleUrl: './account-change-dialog.component.scss',
    standalone: false
})
export class AccountChangeDialogComponent {

  fg: FormGroup

  constructor(
    public dialogRef: MatDialogRef<AccountChangeDialogComponent>,
    private service: AccountService,
    @Inject(MAT_DIALOG_DATA) public data: AccountSearchEntry | undefined,
    private fb: FormBuilder
  ) {
    this.fg = fb.group({
      firstName: [data?.firstName ?? '', Validators.required],
      lastName: [data?.lastName ?? '', Validators.required],
      email: [data?.email ?? '', Validators.compose([Validators.email])],
      phone: [data?.phone ?? ''],
      mobile: [data?.mobile ?? ''],
    });
  }

  submit() {
    if (!this.fg.valid) return
    let value = this.fg.value
    let name = value.firstName + ' ' + value.lastName
    let request = new AccountSetupRequest(
      new AccountChangeRequest(name, '', undefined),
      new ProfileChangeRequest(
        value.email,
        value.phone,
        value.mobile,
        value.firstName,
        value.lastName,
        undefined,
        undefined,
        undefined,
        undefined,
        '')
    )
    let observable = this.data ? this.service.updateAccount(+this.data.id, request) : this.service.setupAccount(request)
    observable.subscribe({
      next: a => this.dialogRef.close(a),
      error: err => this.dialogRef.close()
    })

  }
}
