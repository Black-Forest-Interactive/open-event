import {Component, inject} from '@angular/core';
import {Participant, Registration} from "@open-event-workspace/core";
import {MAT_DIALOG_DATA, MatDialogActions, MatDialogContent, MatDialogRef, MatDialogTitle} from "@angular/material/dialog";
import {RegistrationService} from "@open-event-workspace/backoffice";
import {MatButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {TranslatePipe} from "@ngx-translate/core";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatFormField, MatInput} from "@angular/material/input";
import {MatLabel} from "@angular/material/form-field";

@Component({
  selector: 'app-registration-participant-edit-dialog',
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
    MatFormField,
    MatLabel
  ],
  templateUrl: './registration-participant-edit-dialog.component.html',
  styleUrl: './registration-participant-edit-dialog.component.scss'
})
export class RegistrationParticipantEditDialogComponent {
  data: { registration: Registration, participant: Participant } = inject(MAT_DIALOG_DATA)
  fg: FormGroup

  constructor(
    fb: FormBuilder,
    private service: RegistrationService,
    public dialogRef: MatDialogRef<RegistrationParticipantEditDialogComponent>
  ) {
    this.fg = fb.group({
      size: [this.data.participant.size, Validators.compose([Validators.required, Validators.min(1)])]
    })
  }

  onCancelClick(): void {
    this.dialogRef.close(null)
  }

  onSaveClick() {
    if (!this.fg.valid) return
    let request = this.fg.value
    this.service.changeParticipant(this.data.registration.id, this.data.participant.id, request).subscribe(
      {
        next: val => this.dialogRef.close(val),
        error: err => this.dialogRef.close(null),
      }
    )
  }
}
