import { Component, inject } from '@angular/core'
import { AudienceService } from '@open-event/admin'
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog'
import { Audience } from '@open-event/core'
import { MatFormFieldModule } from '@angular/material/form-field'
import { MatInputModule } from '@angular/material/input'
import { MatButtonModule } from '@angular/material/button'
import { MatIconModule } from '@angular/material/icon'
import { TranslatePipe } from '@ngx-translate/core'
import { ReactiveFormsModule } from '@angular/forms'

@Component({
  selector: 'admin-audience-delete-dialog',
  imports: [MatDialogModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatIconModule, TranslatePipe, ReactiveFormsModule],
  templateUrl: './audience-delete-dialog.component.html',
  styleUrl: './audience-delete-dialog.component.scss'
})
export class AudienceDeleteDialogComponent {
  dialogRef = inject<MatDialogRef<AudienceDeleteDialogComponent>>(MatDialogRef)
  data = inject<Audience>(MAT_DIALOG_DATA)
  private service = inject(AudienceService)

  onNoClick(): void {
    this.dialogRef.close(false)
  }

  onYesClick() {
    this.service.deleteAudience(this.data.id).subscribe({
      next: () => this.dialogRef.close(true),
      error: () => this.dialogRef.close(true)
    })
  }
}
