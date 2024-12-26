import {Component} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {ActivityInfo} from "../model/activity-api";
import {ActivityService} from "../model/activity.service";
import {HotToastService} from "@ngxpert/hot-toast";
import {Page} from "../../shared/model/page";
import {PageEvent} from "@angular/material/paginator";

@Component({
    selector: 'app-activity-table',
    templateUrl: './activity-table.component.html',
    styleUrl: './activity-table.component.scss',
    standalone: false
})
export class ActivityTableComponent {
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
    this.service.markAllRead().subscribe({
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
