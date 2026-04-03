import { Component, inject } from '@angular/core'
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog'
import { MatInputModule } from '@angular/material/input'
import { MatButtonModule } from '@angular/material/button'
import { MatIconModule } from '@angular/material/icon'
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms'
import { TranslateModule } from '@ngx-translate/core'
import { ExternalParticipantConfirmRequest } from '@open-event/external'

@Component({
  selector: 'app-confirm-participation-dialog',
  imports: [MatDialogModule, MatInputModule, MatButtonModule, MatIconModule, ReactiveFormsModule, TranslateModule],
  templateUrl: './confirm-participation-dialog.component.html',
  styleUrl: './confirm-participation-dialog.component.scss',
})
export class ConfirmParticipationDialogComponent {
  dialogRef = inject(MatDialogRef<ConfirmParticipationDialogComponent>)
  private fb = inject(FormBuilder)

  fg: FormGroup = this.fb.group({
    code: ['', Validators.required],
  })

  submit() {
    if (!this.fg.valid) return
    const value = this.fg.value
    this.dialogRef.close(new ExternalParticipantConfirmRequest(value.code))
  }
}
