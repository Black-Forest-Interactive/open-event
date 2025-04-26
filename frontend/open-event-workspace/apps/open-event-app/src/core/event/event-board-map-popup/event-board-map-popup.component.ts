import {Component, EventEmitter, Input, Output} from '@angular/core';
import {MatDialogActions, MatDialogContent, MatDialogTitle} from "@angular/material/dialog";
import {AccountComponent} from "../../account/account/account.component";
import {AccountDisplayNamePipe, EventSearchEntry} from "@open-event-workspace/core";
import {DatePipe} from "@angular/common";
import {MatIcon} from "@angular/material/icon";
import {TranslatePipe} from "@ngx-translate/core";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-event-board-map-popup',
  templateUrl: './event-board-map-popup.component.html',
  styleUrls: ['./event-board-map-popup.component.scss'],
  imports: [
    MatDialogContent,
    AccountComponent,
    MatDialogTitle,
    AccountDisplayNamePipe,
    DatePipe,
    MatDialogActions,
    MatIcon,
    TranslatePipe,
    MatButton,
  ],
  standalone: true
})
export class EventBoardMapPopupComponent {

  @Output() close: EventEmitter<boolean> = new EventEmitter()
  @Input() data: EventSearchEntry | undefined


  ngOnInit(): void {

  }

  onDetailsClick() {
    this.close.emit(true)
  }

  onCloseClick() {
    this.close.emit(false)
  }
}
