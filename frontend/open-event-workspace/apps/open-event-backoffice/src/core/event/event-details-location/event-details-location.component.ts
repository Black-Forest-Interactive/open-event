import {Component, input} from '@angular/core';
import {EventInfo, LocationMapComponent} from "@open-event-workspace/core";
import {BoardCardComponent} from "../../../shared/board-card/board-card.component";
import {TranslatePipe} from "@ngx-translate/core";

@Component({
  selector: 'app-event-details-location',
  imports: [
    BoardCardComponent,
    TranslatePipe,
    LocationMapComponent
  ],
  templateUrl: './event-details-location.component.html',
  styleUrl: './event-details-location.component.scss'
})
export class EventDetailsLocationComponent {
  event = input.required<EventInfo>()
}
