import { Component } from '@angular/core'
import { ActivityTableComponent } from './activity-table/activity-table.component'
import { MatCard } from '@angular/material/card'
import { MatIcon } from '@angular/material/icon'
import { TranslatePipe } from '@ngx-translate/core'

@Component({
  selector: 'portal-activity',
  imports: [ActivityTableComponent, MatCard, MatIcon, TranslatePipe],
  templateUrl: './activity.component.html',
  styleUrl: './activity.component.scss'
})
export class ActivityComponent {}
