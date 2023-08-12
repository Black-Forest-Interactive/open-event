import {Component, EventEmitter} from '@angular/core';
import {EventBoardService} from "../model/event-board.service";
import {debounceTime, distinctUntilChanged} from "rxjs";

@Component({
  selector: 'app-event-board-header',
  templateUrl: './event-board-header.component.html',
  styleUrls: ['./event-board-header.component.scss']
})
export class EventBoardHeaderComponent {

  keyUp: EventEmitter<string> = new EventEmitter<string>()

  constructor(public service: EventBoardService) {
  }

  ngOnInit() {
    this.keyUp.pipe(
      debounceTime(500),
      distinctUntilChanged()
    ).subscribe(query => this.search(query))
  }

  search(query: string) {
    this.service.search(query)
  }
}
