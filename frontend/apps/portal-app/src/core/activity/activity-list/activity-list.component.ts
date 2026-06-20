import { Component, input, output } from '@angular/core'
import { CommonModule } from '@angular/common'
import { ActivityInfo } from '@open-event/core'
import { MatIcon } from '@angular/material/icon'
import { getActivityIcon } from '@open-event/ui'

@Component({
  selector: 'portal-activity-list',
  imports: [CommonModule, MatIcon],
  templateUrl: './activity-list.component.html',
  styleUrl: './activity-list.component.scss'
})
export class ActivityListComponent {
  data = input.required<ActivityInfo[]>()
  clicked = output<ActivityInfo>()

  readonly getActivityIcon = getActivityIcon
}
