import { Component, inject, OnInit } from '@angular/core'

import { Feedback } from '@open-event/core'
import { FeedbackService } from '@open-event/admin'
import { HotToastService } from '@ngxpert/hot-toast'
import { Page } from '@open-event/shared'
import { PageEvent } from '@angular/material/paginator'
import { BoardComponent, BoardToolbarActions } from '../../shared/board/board.component'
import { MatCard } from '@angular/material/card'
import { FeedbackTableComponent } from './feedback-table/feedback-table.component'

@Component({
  selector: 'admin-feedback',
  imports: [BoardComponent, BoardToolbarActions, MatCard, FeedbackTableComponent],
  templateUrl: './feedback.component.html',
  styleUrl: './feedback.component.scss'
})
export class FeedbackComponent implements OnInit {
  reloading: boolean = false
  pageNumber = 0
  pageSize = 25
  totalElements = 0
  data: Feedback[] = []
  private service = inject(FeedbackService)
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
    this.service.getAllFeedbacks(page, size).subscribe({
      next: (value) => this.handleData(value),
      error: (err) => this.handleError(err)
    })
  }

  private handleData(value: Page<Feedback>) {
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
