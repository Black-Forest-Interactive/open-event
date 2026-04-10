import { Component, inject, OnInit } from '@angular/core'

import { IssueService } from '@open-event/admin'
import { PageEvent } from '@angular/material/paginator'
import { Issue } from '@open-event/core'
import { Page } from '@open-event/shared'
import { BoardComponent, BoardToolbarActions } from '../../shared/board/board.component'
import { MatCard } from '@angular/material/card'
import { HotToastService } from '@ngxpert/hot-toast'
import { IssueTableComponent } from './issue-table/issue-table.component'

@Component({
  selector: 'admin-issue',
  imports: [BoardComponent, BoardToolbarActions, MatCard, IssueTableComponent],
  templateUrl: './issue.component.html',
  styleUrl: './issue.component.scss'
})
export class IssueComponent implements OnInit {
  reloading: boolean = false
  pageNumber = 0
  pageSize = 25
  totalElements = 0
  data: Issue[] = []
  private service = inject(IssueService)
  private toast = inject(HotToastService)

  ngOnInit(): void {
    this.reload()
  }

  reload() {
    this.load(0, this.pageSize)
  }

  handlePageChange(event: PageEvent) {
    if (this.reloading) return
    this.pageSize = event.pageSize
    this.load(event.pageIndex, event.pageSize)
  }

  private load(page: number, size: number) {
    if (this.reloading) return
    this.reloading = true
    this.service.getAllIssues(page, size).subscribe({
      next: (value) => this.handleData(value),
      error: (err) => this.handleError(err)
    })
  }

  private handleData(value: Page<Issue>) {
    this.data = value.content
    this.totalElements = value.totalSize
    this.pageNumber = value.pageable.number
    this.reloading = false
  }

  private handleError(err: any) {
    if (err) this.toast.error()
    this.reloading = false
  }
}
