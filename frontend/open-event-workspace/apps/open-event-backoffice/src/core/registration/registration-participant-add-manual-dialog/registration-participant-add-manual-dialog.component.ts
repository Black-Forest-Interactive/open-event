import {Component, inject} from '@angular/core';
import {Registration} from "@open-event-workspace/core";
import {MAT_DIALOG_DATA, MatDialogActions, MatDialogContent, MatDialogRef, MatDialogTitle} from "@angular/material/dialog";
import {RegistrationService} from "@open-event-workspace/backoffice";
import {TranslatePipe} from "@ngx-translate/core";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {atLeastOneRequiredValidator} from '@open-event-workspace/shared';
import {MatButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";

@Component({
  selector: 'app-registration-participant-add-manual-dialog',
  imports: [
    MatButton,
    MatDialogActions,
    MatDialogContent,
    MatDialogTitle,
    MatIcon,
    TranslatePipe,
    ReactiveFormsModule,
    MatFormField,
    MatInput,
    MatLabel
  ],
  templateUrl: './registration-participant-add-manual-dialog.component.html',
  styleUrl: './registration-participant-add-manual-dialog.component.scss'
})
export class RegistrationParticipantAddManualDialogComponent {

  data: { registration: Registration } = inject(MAT_DIALOG_DATA)
  fg: FormGroup

  constructor(
    fb: FormBuilder,
    private service: RegistrationService,
    public dialogRef: MatDialogRef<RegistrationParticipantAddManualDialogComponent>
  ) {
    this.fg = fb.group({
        firstName: ['', Validators.required],
        lastName: ['', Validators.required],
        email: ['', Validators.email],
        phone: [''],
        mobile: [''],
        size: [0, Validators.compose([Validators.required, Validators.min(1)])]
      },
      {
        validators: [atLeastOneRequiredValidator(['email', 'phone', 'mobile'])]
      })
  }

  onCancelClick(): void {
    this.dialogRef.close(null)
  }

  onSaveClick() {
    if (!this.fg.valid) return
    let request = this.fg.value
    this.service.addParticipantManual(this.data.registration.id, request).subscribe(
      {
        next: val => this.dialogRef.close(val),
        error: err => this.dialogRef.close(null),
      }
    )
  }

}
