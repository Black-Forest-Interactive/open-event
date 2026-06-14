import { Component, input } from '@angular/core'
import { EventInfo } from '@open-event/core'
import { AccountComponent } from '../../account/account/account.component'
import { DatePipe } from '@angular/common'
import { MatDivider } from '@angular/material/divider'
import { MatChip, MatChipSet } from '@angular/material/chips'
import { MatIcon } from '@angular/material/icon'

@Component({
  selector: 'portal-event-details-info',
  templateUrl: './event-details-info.component.html',
  styleUrl: './event-details-info.component.scss',
  imports: [AccountComponent, DatePipe, MatDivider, MatChipSet, MatChip, MatIcon],
  standalone: true
})
export class EventDetailsInfoComponent {
  info = input.required<EventInfo>()
}
