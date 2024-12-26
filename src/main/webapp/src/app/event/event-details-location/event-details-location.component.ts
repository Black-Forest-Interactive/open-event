import {Component, Input} from '@angular/core';
import {EventInfo} from "../model/event-api";

@Component({
    selector: 'app-event-details-location',
    templateUrl: './event-details-location.component.html',
    styleUrl: './event-details-location.component.scss',
    standalone: false
})
export class EventDetailsLocationComponent {
  @Input() info: EventInfo | undefined
}
