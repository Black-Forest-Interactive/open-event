import { Component, input } from '@angular/core'
import { EventInfo } from '@open-event/core'
import { LocationMapComponent } from '@open-event/ui'
import { BoardCardComponent } from '../../../shared/board-card/board-card.component'
import { TranslatePipe } from '@ngx-translate/core'

@Component({
  selector: 'admin-event-details-location',
  imports: [BoardCardComponent, TranslatePipe, LocationMapComponent],
  templateUrl: './event-details-location.component.html',
  styleUrl: './event-details-location.component.scss'
})
export class EventDetailsLocationComponent {
  event = input.required<EventInfo>()
}
