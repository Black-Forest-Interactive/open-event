import { Component, computed, input, output } from '@angular/core'
import { DatePipe } from '@angular/common'
import { ActivityInfo } from '@open-event/core'
import { TranslatePipe } from '@ngx-translate/core'
import { MatIcon } from '@angular/material/icon'
import { getActivityIcon } from '@open-event/ui'
import { ActivityReadComponent } from '../activity-read/activity-read.component'

@Component({
  selector: 'portal-activity-row',
  imports: [DatePipe, MatIcon, TranslatePipe, ActivityReadComponent],
  templateUrl: './activity-row.component.html',
  styleUrl: './activity-row.component.scss'
})
export class ActivityRowComponent {
  info = input.required<ActivityInfo>()
  changed = output<ActivityInfo>()

  readonly icon = computed(() => getActivityIcon(this.info().activity.type))
}
