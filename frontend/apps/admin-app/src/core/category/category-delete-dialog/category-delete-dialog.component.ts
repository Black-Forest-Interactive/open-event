import { Component, inject } from '@angular/core'
import { CategoryService } from '@open-event/admin'
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog'
import { Category } from '@open-event/core'

import { MatFormFieldModule } from '@angular/material/form-field'
import { MatInputModule } from '@angular/material/input'
import { MatButtonModule } from '@angular/material/button'
import { MatIconModule } from '@angular/material/icon'
import { TranslatePipe } from '@ngx-translate/core'
import { ReactiveFormsModule } from '@angular/forms'

@Component({
  selector: 'admin-category-delete-dialog',
  imports: [MatDialogModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatIconModule, TranslatePipe, ReactiveFormsModule],
  templateUrl: './category-delete-dialog.component.html',
  styleUrl: './category-delete-dialog.component.scss'
})
export class CategoryDeleteDialogComponent {
  private service = inject(CategoryService)
  dialogRef = inject<MatDialogRef<CategoryDeleteDialogComponent>>(MatDialogRef)
  data = inject<Category>(MAT_DIALOG_DATA)

  onNoClick(): void {
    this.dialogRef.close(false)
  }

  onYesClick() {
    this.service.deleteCategory(this.data.id).subscribe({
      next: () => this.dialogRef.close(true),
      error: () => this.dialogRef.close(true)
    })
  }
}
