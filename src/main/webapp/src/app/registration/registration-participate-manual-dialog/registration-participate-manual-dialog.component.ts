import {Component} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatDialogRef} from "@angular/material/dialog";
import {ParticipantAddRequest} from "../model/registration-api";

@Component({
  selector: 'app-registration-participate-manual-dialog',
  templateUrl: './registration-participate-manual-dialog.component.html',
  styleUrls: ['./registration-participate-manual-dialog.component.scss']
})
export class RegistrationParticipateManualDialogComponent {

  fg: FormGroup

  constructor(
    public dialogRef: MatDialogRef<RegistrationParticipateManualDialogComponent>,
    private fb: FormBuilder
  ) {
    this.fg = fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', Validators.compose([Validators.required, Validators.email])],
      size: [0, Validators.compose([Validators.required, Validators.min(1)])]
    });
  }

  submit() {
    if (!this.fg.valid) return
    let value = this.fg.value
    this.dialogRef.close(new ParticipantAddRequest(value.firstName, value.lastName, value.email, value.size))
  }
}
