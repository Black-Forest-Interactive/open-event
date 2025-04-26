import {Component, input} from '@angular/core';
import {MatIcon} from "@angular/material/icon";
import {Event} from '../event.api'

@Component({
  selector: 'lib-event-published-icon',
  imports: [
    MatIcon
  ],
  templateUrl: './event-published-icon.component.html',
  styleUrl: './event-published-icon.component.scss'
})
export class EventPublishedIconComponent {
  event = input.required<Event>()
}
