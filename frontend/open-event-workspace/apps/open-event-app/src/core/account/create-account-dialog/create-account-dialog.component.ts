import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatDialogActions, MatDialogClose, MatDialogContent, MatDialogRef, MatDialogTitle} from '@angular/material/dialog';
import {AccountChangeRequest, AccountService, AccountSetupRequest, ProfileChangeRequest} from '@open-event-workspace/core';
import {TranslatePipe} from "@ngx-translate/core";
import {MatFormField} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-create-account-dialog',
  imports: [CommonModule, TranslatePipe, ReactiveFormsModule, MatFormField, MatInput, MatDialogContent, MatDialogTitle, MatDialogActions, MatButton, MatDialogClose],
  templateUrl: './create-account-dialog.component.html',
  styleUrl: './create-account-dialog.component.scss',
})
export class CreateAccountDialogComponent {
  fg: FormGroup

  constructor(
    public dialogRef: MatDialogRef<CreateAccountDialogComponent>,
    private service: AccountService,
    fb: FormBuilder
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
