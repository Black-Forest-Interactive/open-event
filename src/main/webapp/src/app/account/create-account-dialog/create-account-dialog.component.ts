import {Component} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatDialogRef} from "@angular/material/dialog";
import {AccountChangeRequest, AccountSetupRequest} from "../model/account-api";
import {ProfileChangeRequest} from "../../profile/model/profile-api";
import {AccountService} from "../model/account.service";

@Component({
    selector: 'app-create-account-dialog',
    templateUrl: './create-account-dialog.component.html',
    styleUrls: ['./create-account-dialog.component.scss'],
    standalone: false
})
export class CreateAccountDialogComponent {

  fg: FormGroup

  constructor(
    public dialogRef: MatDialogRef<CreateAccountDialogComponent>,
    private service: AccountService,
    private fb: FormBuilder
  ) {
    this.fg = fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', Validators.compose([Validators.email])],
      phone: [''],
      mobile: [''],
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

    this.service.setupAccount(request).subscribe({
      next: a => this.dialogRef.close(a),
      error: err => this.dialogRef.close()
    })

  }
}
