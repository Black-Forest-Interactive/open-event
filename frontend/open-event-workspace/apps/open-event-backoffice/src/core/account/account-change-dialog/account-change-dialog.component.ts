import {Component, Inject} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogActions, MatDialogClose, MatDialogContent, MatDialogRef, MatDialogTitle} from "@angular/material/dialog";
import {AccountChangeRequest, AccountSearchEntry, AccountSetupRequest, ProfileChangeRequest} from "@open-event-workspace/core";
import {CommonModule} from "@angular/common";
import {TranslatePipe} from "@ngx-translate/core";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatIcon} from "@angular/material/icon";
import {MatInput} from "@angular/material/input";
import {MatButton} from "@angular/material/button";
import {AccountService} from "@open-event-workspace/backoffice";

@Component({
  selector: 'app-account-change-dialog',
  imports: [CommonModule, TranslatePipe, ReactiveFormsModule, MatFormField, MatDialogActions, MatIcon, MatInput, MatLabel, MatDialogClose, MatButton, MatDialogTitle, MatDialogContent],
  templateUrl: './account-change-dialog.component.html',
  styleUrl: './account-change-dialog.component.scss'
})
export class AccountChangeDialogComponent {

  fg: FormGroup

  constructor(
    public dialogRef: MatDialogRef<AccountChangeDialogComponent>,
    private service: AccountService,
    @Inject(MAT_DIALOG_DATA) public data: AccountSearchEntry | undefined,
    fb: FormBuilder
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
    let observable = this.data ? this.service.updateSetupAccount(+this.data.id, request) : this.service.setupAccount(request)
    observable.subscribe({
      next: a => this.dialogRef.close(a),
      error: err => this.dialogRef.close()
    })

  }
}
