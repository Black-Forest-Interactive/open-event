import {Component, input} from '@angular/core';
import {AccountDisplayNamePipe, EventSearchEntry} from "@open-event-workspace/core";
import {MatCard} from "@angular/material/card";
import {RouterLink} from "@angular/router";
import {MatIcon} from "@angular/material/icon";
import {DatePipe} from "@angular/common";
import {RegistrationStatusComponent} from "../../registration/registration-status/registration-status.component";

@Component({
  selector: 'app-event-board-list-entry',
  templateUrl: './event-board-list-entry.component.html',
  styleUrls: ['./event-board-list-entry.component.scss'],
  imports: [
    MatCard,
    RouterLink,
    MatIcon,
    AccountDisplayNamePipe,
    DatePipe,
    RegistrationStatusComponent
  ],
  standalone: true
})
export class EventBoardListEntryComponent {
  info = input.required<EventSearchEntry>()
}
