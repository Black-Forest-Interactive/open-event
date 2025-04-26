import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ActivityService} from "@open-event-workspace/backoffice";
import {Activity, ActivityCleanupRequest} from "@open-event-workspace/core";
import {Page} from "@open-event-workspace/shared";
import {HotToastService} from "@ngxpert/hot-toast";
import {MatCard} from "@angular/material/card";
import {MatIcon} from "@angular/material/icon";
import {MatButton} from "@angular/material/button";
import {MatTableModule} from "@angular/material/table";
import {PageEvent} from "@angular/material/paginator";
import {ActivityTableComponent} from "./activity-table/activity-table.component";
import {MatDialog} from "@angular/material/dialog";
import {ActivityCleanupDialogComponent} from "./activity-cleanup-dialog/activity-cleanup-dialog.component";
import {BoardComponent, BoardToolbarActions} from "../../shared/board/board.component";

@Component({
  selector: 'boffice-activity',
  imports: [CommonModule, MatCard, MatIcon, MatTableModule, ActivityTableComponent, BoardComponent, BoardToolbarActions, MatButton],
  templateUrl: './activity.component.html',
  styleUrl: './activity.component.scss',
})
export class ActivityComponent implements OnInit {

  reloading: boolean = false
  pageNumber = 0
  pageSize = 25
  totalElements = 0

  data: Activity[] = []

  constructor(private service: ActivityService, private toast: HotToastService, private dialog: MatDialog) {
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
    this.service.getAllActivities(page, size).subscribe({
      next: value => this.handleData(value),
      error: err => this.handleError(err)
    })
  }

  private handleData(value: Page<Activity>) {
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

  cleanup() {
    let ref = this.dialog.open(ActivityCleanupDialogComponent)
    ref.afterClosed().subscribe(value => {
        if (value) this.runCleanupJob(value)
      }
    )
  }

  private runCleanupJob(request: ActivityCleanupRequest) {
    this.service.cleanup(request).subscribe({
      next: value => this.reload(),
      error: err => this.handleError(err)
    })
  }
}
