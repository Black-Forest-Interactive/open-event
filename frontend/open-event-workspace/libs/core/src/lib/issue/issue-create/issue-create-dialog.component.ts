import {Component, inject} from "@angular/core";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {FormBuilder, FormGroup, ReactiveFormsModule} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogActions, MatDialogClose, MatDialogContent, MatDialogRef, MatDialogTitle} from "@angular/material/dialog";
import {HttpErrorResponse} from "@angular/common/http";
import {BaseIssueService, IssueChangeRequest} from "@open-event-workspace/core";
import {MatButton} from "@angular/material/button";
import {TranslatePipe} from "@ngx-translate/core";
import {MatIcon} from "@angular/material/icon";
import {MatInput} from "@angular/material/input";

@Component({
  selector: 'lib-issue-create-dialog',
  imports: [MatFormFieldModule, MatDatepickerModule, ReactiveFormsModule, MatButton, MatDialogContent, MatDialogTitle, TranslatePipe, MatDialogActions, MatDialogClose, MatIcon, MatInput],
  templateUrl: './issue-create-dialog.component.html',
  styleUrl: './issue-create-dialog.component.scss'
})
export class IssueCreateDialogComponent {
  data: HttpErrorResponse = inject(MAT_DIALOG_DATA)
  fg: FormGroup
  private service: BaseIssueService = inject(BaseIssueService)

  constructor(
    fb: FormBuilder,
    public dialogRef: MatDialogRef<IssueCreateDialogComponent>
  ) {
    this.fg = fb.group({
      subject: fb.control(''),
      description: fb.control('')
    })
  }

  report() {
    if (!this.fg.valid) return
    let value = this.fg.value
    let error = this.data.status + ' ' + this.data.message
    let url = this.data.url ?? 'unknown'
    let request = new IssueChangeRequest(value.subject ?? '', value.description ?? '', error, url)
    this.service.createIssue(request).subscribe({
      next: () => console.log('Bug reported'),
      error: err => console.error('Failed to report bug', err)
    })
    this.dialogRef.close()
  }

}


