import {Component, Inject} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {ParticipateRequest} from "../model/registration-api";
import {Participant} from "../../participant/model/participant-api";

@Component({
    selector: 'app-registration-edit-dialog',
    templateUrl: './registration-edit-dialog.component.html',
    styleUrls: ['./registration-edit-dialog.component.scss'],
    standalone: false
})
export class RegistrationEditDialogComponent {
  fg: FormGroup

  constructor(
    public dialogRef: MatDialogRef<RegistrationEditDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public participant: Participant | undefined,
    private fb: FormBuilder
  ) {
    this.fg = fb.group({
      size: [participant?.size ?? 0, Validators.required]
    });
  }

  submit() {
    if (!this.fg.valid) return
    let value = this.fg.value
    this.dialogRef.close(new ParticipateRequest(value.size))
  }
}
