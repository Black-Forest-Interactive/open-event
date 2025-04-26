import {Component, inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogActions, MatDialogContent, MatDialogRef, MatDialogTitle} from "@angular/material/dialog";
import {TranslatePipe} from "@ngx-translate/core";
import {RegistrationService} from "@open-event-workspace/backoffice";
import {AccountDisplayNamePipe, Participant, Registration} from "@open-event-workspace/core";
import {MatIcon} from "@angular/material/icon";
import {MatButton} from "@angular/material/button";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-registration-participant-remove-dialog',
  imports: [
    MatDialogActions,
    MatDialogContent,
    MatDialogTitle,
    MatIcon,
    TranslatePipe,
    MatButton,
    AccountDisplayNamePipe,
    DatePipe
  ],
  templateUrl: './registration-participant-remove-dialog.component.html',
  styleUrl: './registration-participant-remove-dialog.component.scss'
})
export class RegistrationParticipantRemoveDialogComponent {

  data: { registration: Registration, participant: Participant } = inject(MAT_DIALOG_DATA)

  constructor(
    private service: RegistrationService,
    public dialogRef: MatDialogRef<RegistrationParticipantRemoveDialogComponent>
  ) {

  }

  onCancelClick(): void {
    this.dialogRef.close(null)
  }

  onSaveClick() {
    this.service.removeParticipant(this.data.registration.id, this.data.participant.id).subscribe(
      {
        next: val => this.dialogRef.close(val),
        error: err => this.dialogRef.close(null),
      }
    )
  }
}
