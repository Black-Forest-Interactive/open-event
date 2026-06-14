import { Component, inject } from '@angular/core'
import { MAT_DIALOG_DATA, MatDialogContent } from '@angular/material/dialog'
import { Issue } from '@open-event/core'
import { IssueCardComponent } from '../issue-card/issue-card.component'
import { TranslatePipe } from '@ngx-translate/core'

@Component({
  selector: 'admin-issue-details-dialog',
  imports: [IssueCardComponent, MatDialogContent, TranslatePipe],
  templateUrl: './issue-details-dialog.component.html',
  styleUrl: './issue-details-dialog.component.scss'
})
export class IssueDetailsDialogComponent {
  issue: Issue = inject(MAT_DIALOG_DATA)
}
