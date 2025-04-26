import {Component, input} from '@angular/core';
import {EventInfo} from '@open-event-workspace/core';
import {EventDetailsInfoComponent} from "../event-details-info/event-details-info.component";
import {EventDetailsLocationComponent} from "../event-details-location/event-details-location.component";

@Component({
  selector: 'app-event-details-overview',
  imports: [
    EventDetailsInfoComponent,
    EventDetailsLocationComponent
  ],
  templateUrl: './event-details-overview.component.html',
  styleUrl: './event-details-overview.component.scss'
})
export class EventDetailsOverviewComponent {
  event = input.required<EventInfo>()
}
