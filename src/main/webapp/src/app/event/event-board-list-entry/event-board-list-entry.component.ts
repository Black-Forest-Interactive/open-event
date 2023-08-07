import {Component, Input} from '@angular/core';
import {EventInfo} from "../model/event-api";

@Component({
  selector: 'app-event-board-list-entry',
  templateUrl: './event-board-list-entry.component.html',
  styleUrls: ['./event-board-list-entry.component.scss']
})
export class EventBoardListEntryComponent {
  @Input() info: EventInfo | undefined
}
