import {Component, input} from '@angular/core';
import {EventInfo} from "@open-event-workspace/core";
import {MatCard, MatCardHeader, MatCardImage, MatCardSubtitle, MatCardTitle} from "@angular/material/card";
import {MatDivider} from "@angular/material/divider";
import {LocationMapComponent} from "../../location/location-map/location-map.component";

@Component({
  selector: 'app-event-details-location',
  templateUrl: './event-details-location.component.html',
  styleUrl: './event-details-location.component.scss',
  imports: [
    MatCard,
    MatCardHeader,
    MatDivider,
    LocationMapComponent,
    MatCardImage,
    MatCardTitle,
    MatCardSubtitle,
  ],
  standalone: true
})
export class EventDetailsLocationComponent {
  info = input.required<EventInfo>()
}
