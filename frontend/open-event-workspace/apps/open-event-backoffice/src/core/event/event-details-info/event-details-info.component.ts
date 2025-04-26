import {Component, input} from '@angular/core';
import {EventInfo, EventPublishedIconComponent} from "@open-event-workspace/core";
import {BoardCardComponent} from "../../../shared/board-card/board-card.component";
import {TranslatePipe} from "@ngx-translate/core";

@Component({
  selector: 'app-event-details-info',
  imports: [
    BoardCardComponent,
    TranslatePipe,
    EventPublishedIconComponent
  ],
  templateUrl: './event-details-info.component.html',
  styleUrl: './event-details-info.component.scss'
})
export class EventDetailsInfoComponent {
  event = input.required<EventInfo>()
}
