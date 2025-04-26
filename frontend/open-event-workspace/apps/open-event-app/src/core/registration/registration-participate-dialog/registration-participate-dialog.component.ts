import {Component} from '@angular/core';
import {MatDialogActions, MatDialogClose, MatDialogContent, MatDialogRef, MatDialogTitle} from "@angular/material/dialog";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {TranslatePipe} from "@ngx-translate/core";
import {MatButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {ParticipateRequest} from "@open-event-workspace/core";

@Component({
  selector: 'app-registration-participate-dialog',
  templateUrl: './registration-participate-dialog.component.html',
  styleUrls: ['./registration-participate-dialog.component.scss'],
  imports: [
    MatDialogTitle,
    MatDialogContent,
    TranslatePipe,
    MatDialogActions,
    MatButton,
    MatIcon,
    MatDialogClose,
    ReactiveFormsModule,
    MatFormField,
    MatInput,
    MatLabel
  ],
  standalone: true
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
