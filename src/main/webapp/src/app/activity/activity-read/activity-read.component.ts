import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Activity, ActivityInfo} from "../model/activity-api";
import {ActivityService} from "../model/activity.service";

@Component({
    selector: 'app-activity-read',
    templateUrl: './activity-read.component.html',
    styleUrl: './activity-read.component.scss',
    standalone: false
})
export class ActivityReadComponent {
  @Input() info!: ActivityInfo
  reloading: boolean = false
  @Output() changed = new EventEmitter<ActivityInfo>()

  constructor(private service: ActivityService) {
  }

  markRead() {
    if (this.reloading) return
    this.reloading = true
    this.service.markRead(this.info.activity.id).subscribe({
      next: value => this.handleData(value),
      error: err => this.handleError(err)
    })
  }

  private handleData(value: Activity) {
    this.info.activity = value
    this.info.read = true
    this.reloading = false
    this.changed.emit(this.info)
  }

  private handleError(err: any) {
    this.reloading = false
  }
}
