import { Component, inject, input, output, signal } from '@angular/core'
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
  info = input.required<ActivityInfo>()
  changed = output<ActivityInfo>()
  readonly reloading = signal(false)
  private service = inject(ActivityService)

  markRead() {
    if (this.reloading()) return
    this.reloading.set(true)
    this.service.markReadSingle(this.info().activity.id).subscribe({
      next: (value) => this.handleData(value),
      error: () => this.reloading.set(false)
    })
  }

  private handleData(value: Activity) {
    this.info().activity = value
    this.info().read = true
    this.reloading.set(false)
    this.changed.emit(this.info())
  }
}
