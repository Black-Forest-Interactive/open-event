import { Component, input, ChangeDetectionStrategy } from '@angular/core'
import { EventInfo } from '@open-event/core'
import { LocationMapComponent } from '@open-event/ui'
import { TranslatePipe } from '@ngx-translate/core'
import { MatCard } from '@angular/material/card'

@Component({
  selector: 'admin-event-details-location',
  imports: [TranslatePipe, LocationMapComponent, MatCard],
  templateUrl: './event-details-location.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './event-details-location.component.scss'
})
export class EventDetailsLocationComponent {
  event = input.required<EventInfo>()
}
