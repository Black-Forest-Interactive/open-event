import {Component} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ParticipateRequest} from "../model/registration-api";

@Component({
    selector: 'app-registration-participate-dialog',
    templateUrl: './registration-participate-dialog.component.html',
    styleUrls: ['./registration-participate-dialog.component.scss'],
    standalone: false
})
export class RegistrationParticipateDialogComponent {

  fg: FormGroup

  constructor(
    public dialogRef: MatDialogRef<RegistrationParticipateDialogComponent>,
    private fb: FormBuilder
  ) {
    this.fg = fb.group({
      size: [0, Validators.required]
    });
  }

  submit() {
    if (!this.fg.valid) return
    let value = this.fg.value
    this.dialogRef.close(new ParticipateRequest(value.size))
  }
}
