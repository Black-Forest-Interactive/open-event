import {Component} from '@angular/core';
import {EventBoardService} from "../model/event-board.service";

@Component({
  selector: 'app-event-board-list',
  templateUrl: './event-board-list.component.html',
  styleUrls: ['./event-board-list.component.scss']
})
export class EventBoardListComponent {
  constructor(public service: EventBoardService) {
  }
}
