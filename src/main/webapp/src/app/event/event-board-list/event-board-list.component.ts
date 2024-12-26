import {Component} from '@angular/core';
import {EventBoardService} from "../model/event-board.service";

@Component({
    selector: 'app-event-board-list',
    templateUrl: './event-board-list.component.html',
    styleUrls: ['./event-board-list.component.scss'],
    standalone: false
})
export class EventBoardListComponent {

  filterOverlayOpen: boolean = false

  constructor(public service: EventBoardService) {
    this.filterOverlayOpen = service.filterToolbarVisible
  }

}
