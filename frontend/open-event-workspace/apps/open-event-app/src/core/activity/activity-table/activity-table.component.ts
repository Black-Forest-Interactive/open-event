import {Component, OnInit} from '@angular/core';
import {MatCell, MatColumnDef, MatHeaderCell, MatHeaderRow, MatRow, MatTableDataSource, MatTableModule} from "@angular/material/table";
import {HotToastService} from "@ngxpert/hot-toast";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {MatCard} from "@angular/material/card";
import {TranslatePipe} from "@ngx-translate/core";
import {MatIcon} from "@angular/material/icon";
import {MatButton} from "@angular/material/button";
import {DatePipe, NgClass} from "@angular/common";
import {MatDivider} from "@angular/material/divider";
import {ActivityInfo} from "@open-event-workspace/core";
import {LoadingBarComponent, Page} from "@open-event-workspace/shared";
import {ActivityReadComponent} from "../activity-read/activity-read.component";
import {ActivityService} from "@open-event-workspace/app";

@Component({
  selector: 'app-activity-table',
  templateUrl: './activity-table.component.html',
  styleUrl: './activity-table.component.scss',
  imports: [
    MatCard,
    MatTableModule,
    MatColumnDef,
    MatHeaderCell,
    MatCell,
    TranslatePipe,
    MatIcon,
    MatButton,
    NgClass,
    MatDivider,
    MatPaginator,
    ActivityReadComponent,
    DatePipe,
    MatRow,
    MatHeaderRow,
    LoadingBarComponent
  ],
  standalone: true
})
export class ActivityTableComponent implements OnInit {
  reloading: boolean = false
  pageSize: number = 25
  pageIndex: number = 0
  totalSize: number = 0
  unread: number = 0
  displayedColumns: string[] = ['type', 'title', 'actor', 'timestamp', 'read']
  datasource = new MatTableDataSource<ActivityInfo>()

  constructor(
    private service: ActivityService,
    private toast: HotToastService
  ) {
  }

  ngOnInit() {
    this.reload(this.pageIndex, this.pageSize)
  }

  private reload(page: number, size: number) {
    if (this.reloading) return
    this.reloading = true
    this.loadData(page, size);
  }


  private handleData(page: Page<ActivityInfo>) {
    this.datasource.data = page.content
    this.unread = page.content.filter(c => !c.read).length
    this.pageSize = page.pageable.size
    this.pageIndex = page.pageable.number
    this.totalSize = page.totalSize
    this.reloading = false
  }

  private handleError(err: any) {
    this.toast.error("Something went wrong")
    this.reloading = false
  }

  handlePageChange(event: PageEvent) {
    this.reload(event.pageIndex, event.pageSize)
  }

  handleReadStatusChanged(event: ActivityInfo) {
    let data = this.datasource.data
    let index = data.findIndex(d => d.activity.id == event.activity.id)
    if (index < 0) return

    data[index] = event
    this.datasource.data = data
  }

  handleMarkAllReadClick() {
    this.reloading = true
    this.service.markReadAll().subscribe({
      next: value => this.loadData(0, this.pageSize),
      error: err => this.handleError(err)
    })
  }

  private loadData(page: number, size: number) {
    this.service.getRecentActivityInfos(page, size).subscribe({
      next: value => this.handleData(value),
      error: err => this.handleError(err)
    })
  }
}
