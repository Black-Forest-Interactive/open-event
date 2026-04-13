import { Component, input } from '@angular/core'
import { EventInfo } from '@open-event/core'
import { EventPublishedIconComponent, EventSharedIconComponent } from '@open-event/ui'
import { BoardCardComponent } from '../../../shared/board-card/board-card.component'
import { TranslatePipe } from '@ngx-translate/core'
import { DatePipe } from '@angular/common'

@Component({
  selector: 'admin-event-details-info',
  imports: [BoardCardComponent, TranslatePipe, EventPublishedIconComponent, EventSharedIconComponent, DatePipe],
  templateUrl: './event-details-info.component.html',
  styleUrl: './event-details-info.component.scss'
})
export class EventDetailsInfoComponent {
  event = input.required<EventInfo>()
}
