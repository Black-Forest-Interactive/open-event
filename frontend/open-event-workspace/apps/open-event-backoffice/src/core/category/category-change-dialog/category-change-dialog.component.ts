import {Component, Inject} from '@angular/core';
import {ReactiveFormsModule} from "@angular/forms";
import {CategoryService} from "@open-event-workspace/backoffice";
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from "@angular/material/dialog";
import {Category, CategoryChangeComponent, CategoryChangeRequest} from "@open-event-workspace/core";
import {CommonModule} from "@angular/common";
import {TranslatePipe} from "@ngx-translate/core";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";

@Component({
  selector: 'app-category-change-dialog',
  imports: [CommonModule, MatDialogModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatIconModule, TranslatePipe, ReactiveFormsModule, CategoryChangeComponent],
  templateUrl: './category-change-dialog.component.html',
  styleUrl: './category-change-dialog.component.scss'
})
export class CategoryChangeDialogComponent {


  constructor(
    private service: CategoryService,
    public dialogRef: MatDialogRef<CategoryChangeDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Category | undefined
  ) {
  }

  onCancelClick(): void {
    this.dialogRef.close(false)
  }


  handleRequest(request: CategoryChangeRequest) {
    let observable = (this.data) ? this.service.updateCategory(this.data.id, request) : this.service.createCategory(request)
    observable.subscribe({
      next: val => this.dialogRef.close(true),
      error: err => this.dialogRef.close(true),
    })
  }
}
