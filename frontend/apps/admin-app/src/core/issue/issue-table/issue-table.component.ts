import { Component, inject, input, output } from '@angular/core'
import { Issue } from '@open-event/core'
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator'
import { DatePipe } from '@angular/common'
import { MatTableModule } from '@angular/material/table'
import { MatIconModule } from '@angular/material/icon'
import { MatButtonModule } from '@angular/material/button'
import { TranslatePipe } from '@ngx-translate/core'
import { MatDialog } from '@angular/material/dialog'
import { IssueDetailsDialogComponent } from '../issue-details-dialog/issue-details-dialog.component'

@Component({
  selector: 'admin-issue-table',
  imports: [DatePipe, MatTableModule, MatPaginatorModule, MatIconModule, MatButtonModule, TranslatePipe],
  templateUrl: './issue-table.component.html',
  styleUrl: './issue-table.component.scss'
})
export class IssueTableComponent {
  data = input.required<Issue[]>()
  reloading = input.required<boolean>()
  pageNumber = input.required<number>()
  pageSize = input.required<number>()
  totalElements = input.required<number>()
  pageEvent = output<PageEvent>()
  displayedColumns: string[] = ['error', 'actor', 'status', 'timestamp', 'cmd']
  private dialog = inject(MatDialog)

  showDetails(issue: Issue) {
    this.dialog.open(IssueDetailsDialogComponent, { data: issue })
  }
}
