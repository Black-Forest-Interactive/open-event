import {Component} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatDialogRef} from "@angular/material/dialog";
import {AccountChangeRequest} from "../model/account-api";

@Component({
  selector: 'app-create-account-dialog',
  templateUrl: './create-account-dialog.component.html',
  styleUrls: ['./create-account-dialog.component.scss']
})
export class CreateAccountDialogComponent {

  fg: FormGroup

  constructor(
    public dialogRef: MatDialogRef<CreateAccountDialogComponent>,
    private fb: FormBuilder
  ) {
    this.fg = fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', Validators.compose([Validators.required, Validators.email])],
    });
  }

  submit() {
    if (!this.fg.valid) return
    let value = this.fg.value
    let name = value.firstName + ' ' + value.lastName
    this.dialogRef.close(new AccountChangeRequest(name, value.firstName, value.lastName, value.email, ''))
  }
}
