import { Component, effect, inject } from '@angular/core'
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms'
import { AudienceService } from '@open-event/admin'
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog'
import { Audience, AudienceChangeRequest } from '@open-event/core'
import { TranslatePipe } from '@ngx-translate/core'
import { MatFormFieldModule } from '@angular/material/form-field'
import { MatInputModule } from '@angular/material/input'
import { MatButtonModule } from '@angular/material/button'
import { MatIconModule } from '@angular/material/icon'

@Component({
  selector: 'admin-audience-change-dialog',
  imports: [MatDialogModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatIconModule, TranslatePipe, ReactiveFormsModule],
  templateUrl: './audience-change-dialog.component.html',
  styleUrl: './audience-change-dialog.component.scss'
})
export class AudienceChangeDialogComponent {
  dialogRef = inject<MatDialogRef<AudienceChangeDialogComponent>>(MatDialogRef)
  data = inject<Audience | null>(MAT_DIALOG_DATA)
  fg: FormGroup
  private service = inject(AudienceService)
  private fb = inject(FormBuilder)

  constructor() {
    this.fg = this.fb.group({
      name: this.fb.control(this.data?.name ?? '', Validators.required),
      iconUrl: this.fb.control(this.data?.iconUrl ?? '')
    })
  }

  onCancelClick(): void {
    this.dialogRef.close(false)
  }

  submit() {
    if (!this.fg.valid) return
    const value = this.fg.value
    const request = new AudienceChangeRequest(value.name, value.iconUrl)
    const observable = this.data ? this.service.updateAudience(this.data.id, request) : this.service.createAudience(request)
    observable.subscribe({
      next: () => this.dialogRef.close(true),
      error: () => this.dialogRef.close(true)
    })
  }
}
