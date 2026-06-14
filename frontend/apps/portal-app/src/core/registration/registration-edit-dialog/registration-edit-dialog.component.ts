import { Component, inject } from '@angular/core'
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms'
import { MAT_DIALOG_DATA, MatDialogActions, MatDialogClose, MatDialogContent, MatDialogRef, MatDialogTitle } from '@angular/material/dialog'
import { TranslatePipe } from '@ngx-translate/core'
import { MatButton } from '@angular/material/button'
import { MatIcon } from '@angular/material/icon'
import { MatFormField, MatLabel } from '@angular/material/form-field'
import { MatInput } from '@angular/material/input'
import { Participant, ParticipateRequest } from '@open-event/core'

@Component({
  selector: 'portal-registration-edit-dialog',
  templateUrl: './registration-edit-dialog.component.html',
  styleUrls: ['./registration-edit-dialog.component.scss'],
  imports: [MatDialogTitle, MatDialogContent, TranslatePipe, MatDialogActions, MatButton, MatIcon, MatDialogClose, ReactiveFormsModule, MatFormField, MatInput, MatLabel],
  standalone: true
})
export class RegistrationEditDialogComponent {
  dialogRef = inject<MatDialogRef<RegistrationEditDialogComponent>>(MatDialogRef)
  participant = inject<Participant | undefined>(MAT_DIALOG_DATA)
  fg: FormGroup
  private fb = inject(FormBuilder)

  constructor() {
    const participant = this.participant
    const fb = this.fb

    this.fg = fb.group({
      size: [participant?.size ?? 0, Validators.required]
    })
  }

  submit() {
    if (!this.fg.valid) return
    const value = this.fg.value
    this.dialogRef.close(new ParticipateRequest(value.size))
  }
}
