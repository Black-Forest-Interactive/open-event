import {Component, Inject} from '@angular/core';
import {CategoryService} from "@open-event-workspace/backoffice";
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from "@angular/material/dialog";
import {Category} from "@open-event-workspace/core";
import {CommonModule} from "@angular/common";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {TranslatePipe} from "@ngx-translate/core";
import {ReactiveFormsModule} from "@angular/forms";

@Component({
  selector: 'app-category-delete-dialog',
  imports: [CommonModule, MatDialogModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatIconModule, TranslatePipe, ReactiveFormsModule],
  templateUrl: './category-delete-dialog.component.html',
  styleUrl: './category-delete-dialog.component.scss'
})
export class CategoryDeleteDialogComponent {

  constructor(
    private service: CategoryService,
    public dialogRef: MatDialogRef<CategoryDeleteDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Category
  ) {

  }

  onNoClick(): void {
    this.dialogRef.close(false)
  }

  onYesClick() {
    this.service.deleteCategory(this.data.id).subscribe(
      {
        next: val => this.dialogRef.close(true),
        error: err => this.dialogRef.close(true),
      }
    )
  }
}
