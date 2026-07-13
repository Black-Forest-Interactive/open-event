import { Component, input, ChangeDetectionStrategy } from '@angular/core'
import { EventInfo } from '@open-event/core'
import { TranslatePipe } from '@ngx-translate/core'
import { EventFactRowComponent } from '../event-fact-row/event-fact-row.component'

@Component({
  selector: 'portal-event-details-info',
  templateUrl: './event-details-info.component.html',
  styleUrl: './event-details-info.component.scss',
  imports: [TranslatePipe, EventFactRowComponent],
  changeDetection: ChangeDetectionStrategy.Eager,
  standalone: true
})
export class EventDetailsInfoComponent {
  info = input.required<EventInfo>()
}
