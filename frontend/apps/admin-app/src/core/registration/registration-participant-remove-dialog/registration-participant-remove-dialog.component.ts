import { Component, inject } from '@angular/core'
import { MAT_DIALOG_DATA, MatDialogActions, MatDialogContent, MatDialogRef, MatDialogTitle } from '@angular/material/dialog'
import { TranslatePipe } from '@ngx-translate/core'
import { RegistrationService } from '@open-event/admin'
import { AccountDisplayNamePipe, Participant, Registration } from '@open-event/core'
import { MatIcon } from '@angular/material/icon'
import { MatButton } from '@angular/material/button'
import { DatePipe } from '@angular/common'

@Component({
  selector: 'admin-registration-participant-remove-dialog',
  imports: [MatDialogActions, MatDialogContent, MatDialogTitle, MatIcon, TranslatePipe, MatButton, AccountDisplayNamePipe, DatePipe],
  templateUrl: './registration-participant-remove-dialog.component.html',
  styleUrl: './registration-participant-remove-dialog.component.scss'
})
export class RegistrationParticipantRemoveDialogComponent {
  dialogRef = inject<MatDialogRef<RegistrationParticipantRemoveDialogComponent>>(MatDialogRef)
  data: { registration: Registration; participant: Participant } = inject(MAT_DIALOG_DATA)
  private service = inject(RegistrationService)

  onCancelClick(): void {
    this.dialogRef.close(null)
  }

  onSaveClick() {
    this.service.removeParticipant(this.data.registration.id, this.data.participant.id).subscribe({
      next: (val) => this.dialogRef.close(val),
      error: () => this.dialogRef.close(null)
    })
  }
}
