import { Component, inject } from '@angular/core'
import { ReactiveFormsModule } from '@angular/forms'
import { CategoryService } from '@open-event/admin'
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog'
import { Category, CategoryChangeRequest } from '@open-event/core'
import { CategoryChangeComponent } from '@open-event/ui'

import { TranslatePipe } from '@ngx-translate/core'
import { MatFormFieldModule } from '@angular/material/form-field'
import { MatInputModule } from '@angular/material/input'
import { MatButtonModule } from '@angular/material/button'
import { MatIconModule } from '@angular/material/icon'

@Component({
  selector: 'admin-category-change-dialog',
  imports: [MatDialogModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatIconModule, TranslatePipe, ReactiveFormsModule, CategoryChangeComponent],
  templateUrl: './category-change-dialog.component.html',
  styleUrl: './category-change-dialog.component.scss'
})
export class CategoryChangeDialogComponent {
  dialogRef = inject<MatDialogRef<CategoryChangeDialogComponent>>(MatDialogRef)
  data = inject<Category | undefined>(MAT_DIALOG_DATA)
  private service = inject(CategoryService)

  onCancelClick(): void {
    this.dialogRef.close(false)
  }

  handleRequest(request: CategoryChangeRequest) {
    const observable = this.data ? this.service.updateCategory(this.data.id, request) : this.service.createCategory(request)
    observable.subscribe({
      next: () => this.dialogRef.close(true),
      error: () => this.dialogRef.close(true)
    })
  }
}
