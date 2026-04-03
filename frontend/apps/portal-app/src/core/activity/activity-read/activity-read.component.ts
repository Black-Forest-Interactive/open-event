import { Component, inject, input, output } from '@angular/core'
import { MatButton } from '@angular/material/button'
import { TranslatePipe } from '@ngx-translate/core'
import { MatProgressSpinner } from '@angular/material/progress-spinner'
import { Activity, ActivityInfo } from '@open-event/core'
import { MatIcon } from '@angular/material/icon'
import { ActivityService } from '@open-event/portal'

@Component({
  selector: 'portal-activity-read',
  templateUrl: './activity-read.component.html',
  styleUrl: './activity-read.component.scss',
  imports: [MatButton, MatIcon, TranslatePipe, MatProgressSpinner],
  standalone: true
})
export class ActivityReadComponent {
  private service = inject(ActivityService)

  info = input.required<ActivityInfo>()
  reloading: boolean = false
  changed = output<ActivityInfo>()

  markRead() {
    if (this.reloading) return
    this.reloading = true
    this.service.markReadSingle(this.info().activity.id).subscribe({
      next: (value) => this.handleData(value),
      error: (err) => this.handleError(err)
    })
  }

  private handleData(value: Activity) {
    this.info().activity = value
    this.info().read = true
    this.reloading = false
    this.changed.emit(this.info())
  }

  private handleError(err: any) {
    this.reloading = false
  }
}
