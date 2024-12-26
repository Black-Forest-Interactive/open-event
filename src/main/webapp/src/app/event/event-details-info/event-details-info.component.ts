import {Component, Input} from '@angular/core';
import {EventInfo} from "../model/event-api";

@Component({
    selector: 'app-event-details-info',
    templateUrl: './event-details-info.component.html',
    styleUrl: './event-details-info.component.scss',
    standalone: false
})
export class EventDetailsInfoComponent {
  @Input() info: EventInfo | undefined
}
