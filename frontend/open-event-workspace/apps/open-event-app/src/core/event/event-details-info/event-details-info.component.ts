import {Component, input} from '@angular/core';
import {EventInfo} from "@open-event-workspace/core";
import {MatCard, MatCardActions, MatCardContent, MatCardHeader, MatCardImage} from "@angular/material/card";
import {AccountComponent} from "../../account/account/account.component";
import {DatePipe} from "@angular/common";
import {MatDivider} from "@angular/material/divider";
import {MatChip, MatChipListbox} from "@angular/material/chips";
import {MatIcon} from "@angular/material/icon";

@Component({
  selector: 'app-event-details-info',
  templateUrl: './event-details-info.component.html',
  styleUrl: './event-details-info.component.scss',
  imports: [
    MatCard,
    MatCardHeader,
    AccountComponent,
    DatePipe,
    MatDivider,
    MatCardContent,
    MatCardImage,
    MatCardActions,
    MatChipListbox,
    MatChip,
    MatIcon,
  ],
  standalone: true
})
export class EventDetailsInfoComponent {
  info = input.required<EventInfo>()
}
