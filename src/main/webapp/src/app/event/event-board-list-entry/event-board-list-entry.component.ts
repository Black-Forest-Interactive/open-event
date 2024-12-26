import {Component, Input} from '@angular/core';
import {EventSearchEntry} from "../../search/model/search-api";

@Component({
    selector: 'app-event-board-list-entry',
    templateUrl: './event-board-list-entry.component.html',
    styleUrls: ['./event-board-list-entry.component.scss'],
    standalone: false
})
export class EventBoardListEntryComponent {
  @Input() info!: EventSearchEntry
}
