import { Component, input, ChangeDetectionStrategy } from '@angular/core'
import { EventInfo } from '@open-event/core'
import { LocationMapComponent } from '@open-event/ui'
import { TranslatePipe } from '@ngx-translate/core'
import { MatDivider } from '@angular/material/divider'

@Component({
  selector: 'portal-event-details-location',
  templateUrl: './event-details-location.component.html',
  styleUrl: './event-details-location.component.scss',
  imports: [LocationMapComponent, TranslatePipe, MatDivider],
  changeDetection: ChangeDetectionStrategy.Eager,
  standalone: true
})
export class EventDetailsLocationComponent {
  info = input.required<EventInfo>()
}
