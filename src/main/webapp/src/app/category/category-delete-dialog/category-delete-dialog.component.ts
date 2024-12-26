import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Category} from "../model/category-api";
import {CategoryService} from "../model/category.service";

@Component({
    selector: 'app-category-delete-dialog',
    templateUrl: './category-delete-dialog.component.html',
    styleUrl: './category-delete-dialog.component.scss',
    standalone: false
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
