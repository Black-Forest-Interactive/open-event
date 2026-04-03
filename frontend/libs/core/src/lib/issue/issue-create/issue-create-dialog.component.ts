import { Component, inject } from '@angular/core'
import { MatFormFieldModule } from '@angular/material/form-field'
import { MatDatepickerModule } from '@angular/material/datepicker'
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms'
import { MAT_DIALOG_DATA, MatDialogActions, MatDialogClose, MatDialogContent, MatDialogRef, MatDialogTitle } from '@angular/material/dialog'
import { HttpErrorResponse } from '@angular/common/http'
import { BaseIssueService, IssueChangeRequest } from '@open-event/core'
import { MatButton } from '@angular/material/button'
import { TranslatePipe } from '@ngx-translate/core'
import { MatIcon } from '@angular/material/icon'
import { MatInput } from '@angular/material/input'

@Component({
  selector: 'lib-issue-create-dialog',
  imports: [MatFormFieldModule, MatDatepickerModule, ReactiveFormsModule, MatButton, MatDialogContent, MatDialogTitle, TranslatePipe, MatDialogActions, MatDialogClose, MatIcon, MatInput],
  templateUrl: './issue-create-dialog.component.html',
  styleUrl: './issue-create-dialog.component.scss'
})
export class IssueCreateDialogComponent {
  data: HttpErrorResponse = inject(MAT_DIALOG_DATA)
  private fb = inject(FormBuilder)
  private dialogRef = inject(MatDialogRef<IssueCreateDialogComponent>)
  private service: BaseIssueService = inject(BaseIssueService)

  fg: FormGroup = this.fb.group({
    subject: this.fb.control(''),
    description: this.fb.control('')
  })

  report() {
    if (!this.fg.valid) return
    const value = this.fg.value
    const error = this.data.status + ' ' + this.data.message
    const url = this.data.url ?? 'unknown'
    const request = new IssueChangeRequest(value.subject ?? '', value.description ?? '', error, url)
    this.service.createIssue(request).subscribe({
      next: () => console.log('Bug reported'),
      error: (err) => console.error(err)
    })
    this.dialogRef.close()
  }
}
