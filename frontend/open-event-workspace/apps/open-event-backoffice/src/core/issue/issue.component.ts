import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';
import {IssueService} from "@open-event-workspace/backoffice";
import {PageEvent} from "@angular/material/paginator";
import {Issue} from "@open-event-workspace/core";
import {Page} from "@open-event-workspace/shared";
import {BoardComponent, BoardToolbarActions} from "../../shared/board/board.component";
import {MatCard} from "@angular/material/card";
import {HotToastService} from "@ngxpert/hot-toast";
import {IssueTableComponent} from "./issue-table/issue-table.component";

@Component({
  selector: 'app-issue',
  imports: [CommonModule, BoardComponent, BoardToolbarActions, MatCard, IssueTableComponent],
  templateUrl: './issue.component.html',
  styleUrl: './issue.component.scss',
})
export class IssueComponent {

  reloading: boolean = false
  pageNumber = 0
  pageSize = 25
  totalElements = 0

  data: Issue[] = []

  constructor(private service: IssueService, private toast: HotToastService) {
  }

  ngOnInit(): void {
    this.reload()
  }


  reload() {
    this.load(0, this.pageSize)
  }

  private load(page: number, size: number) {
    if (this.reloading) return
    this.reloading = true
    this.service.getAllIssues(page, size).subscribe({
      next: value => this.handleData(value),
      error: err => this.handleError(err)
    })
  }

  private handleData(value: Page<Issue>) {
    this.data = value.content
    this.totalElements = value.totalSize
    this.pageNumber = value.pageable.number
    this.reloading = false
  }

  private handleError(err: any) {
    if (err) this.toast.error(err)
    this.reloading = false
  }

  handlePageChange(event: PageEvent) {
    if (this.reloading) return
    this.pageSize = event.pageSize
    this.load(event.pageIndex, event.pageSize)
  }
}
