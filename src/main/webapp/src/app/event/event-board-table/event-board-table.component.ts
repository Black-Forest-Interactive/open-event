import {Component} from '@angular/core';
import {EventBoardService} from "../model/event-board.service";

@Component({
  selector: 'app-event-board-table',
  templateUrl: './event-board-table.component.html',
  styleUrls: ['./event-board-table.component.scss']
})
export class EventBoardTableComponent {

  displayedColumns: string[] = ['account', 'period', 'location', 'description','status','category', 'publish']
  constructor(public service: EventBoardService) {
  }
}
