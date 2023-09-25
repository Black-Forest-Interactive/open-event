import {Component, EventEmitter, Input, Output} from '@angular/core';
import {EventInfo} from "../model/event-api";

@Component({
  selector: 'app-event-board-map-popup',
  templateUrl: './event-board-map-popup.component.html',
  styleUrls: ['./event-board-map-popup.component.scss']
})
export class EventBoardMapPopupComponent {

  @Output() close: EventEmitter<boolean> = new EventEmitter()
  @Input() data: EventInfo | undefined


  ngOnInit(): void {

  }

  onDetailsClick() {
    this.close.emit(true)
  }

  onCloseClick() {
    this.close.emit(false)
  }
}
