import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Category, CategoryChangeRequest} from "../model/category-api";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CategoryService} from "../model/category.service";

@Component({
    selector: 'app-category-change-dialog',
    templateUrl: './category-change-dialog.component.html',
    styleUrl: './category-change-dialog.component.scss',
    standalone: false
})
export class CategoryChangeDialogComponent {

  fg: FormGroup

  constructor(
    private fb: FormBuilder,
    private service: CategoryService,
    public dialogRef: MatDialogRef<CategoryChangeDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Category | null
  ) {
    this.fg = this.fb.group({
      name: this.fb.control(data?.name ?? '', Validators.required),
      iconUrl: this.fb.control(data?.iconUrl ?? ''),
    })
  }

  onCancelClick(): void {
    this.dialogRef.close(false)
  }

  onSaveClick() {
    if (!this.fg.valid) {
      this.dialogRef.close(false)
    } else {
      let value = this.fg.value
      let request = new CategoryChangeRequest(value.name, value.iconUrl)
      let observable = (this.data) ? this.service.updateCategory(this.data.id, request) : this.service.createCategory(request)
      observable.subscribe({
        next: val => this.dialogRef.close(true),
        error: err => this.dialogRef.close(true),
      })
    }
  }
}
