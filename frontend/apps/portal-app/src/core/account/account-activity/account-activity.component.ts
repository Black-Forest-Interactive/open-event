import { Component } from '@angular/core'

import { ActivityTableComponent } from '../../activity/activity-table/activity-table.component'

@Component({
  selector: 'portal-account-activity',
  imports: [ActivityTableComponent],
  templateUrl: './account-activity.component.html',
  styleUrl: './account-activity.component.scss'
})
export class AccountActivityComponent {}
