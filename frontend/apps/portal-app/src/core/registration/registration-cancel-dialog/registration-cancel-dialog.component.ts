import { Component, inject, signal, ChangeDetectionStrategy } from '@angular/core'
import { FormsModule } from '@angular/forms'
import { MAT_DIALOG_DATA, MatDialogActions, MatDialogClose, MatDialogContent, MatDialogRef, MatDialogTitle } from '@angular/material/dialog'
import { TranslatePipe } from '@ngx-translate/core'
import { MatButton } from '@angular/material/button'
import { MatIcon } from '@angular/material/icon'
import { MatFormField, MatHint, MatLabel } from '@angular/material/form-field'
import { MatInput } from '@angular/material/input'
import { ParticipantCancelRequest } from '@open-event/core'

export interface RegistrationCancelDialogData {
  /** show the optional message to the organizer (self cancel only) */
  withMessage: boolean
}

@Component({
  selector: 'portal-registration-cancel-dialog',
  templateUrl: './registration-cancel-dialog.component.html',
  styleUrls: ['./registration-cancel-dialog.component.scss'],
  imports: [MatDialogTitle, MatDialogContent, TranslatePipe, MatDialogActions, MatButton, MatIcon, MatDialogClose, MatFormField, MatLabel, MatHint, MatInput, FormsModule],
  changeDetection: ChangeDetectionStrategy.Eager
})
export class RegistrationCancelDialogComponent {
  dialogRef = inject<MatDialogRef<RegistrationCancelDialogComponent>>(MatDialogRef)
  private data = inject<RegistrationCancelDialogData | undefined>(MAT_DIALOG_DATA, { optional: true })

  readonly maxLength = 1000
  readonly withMessage = this.data?.withMessage === true
  readonly message = signal('')

  submit() {
    if (!this.withMessage) return this.dialogRef.close(true)
    this.dialogRef.close(new ParticipantCancelRequest(this.message().trim().slice(0, this.maxLength)))
  }
}
