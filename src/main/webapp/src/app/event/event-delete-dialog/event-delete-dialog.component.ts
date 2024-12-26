import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {Event} from "../model/event-api";

@Component({
    selector: 'app-event-delete-dialog',
    templateUrl: './event-delete-dialog.component.html',
    styleUrls: ['./event-delete-dialog.component.scss'],
    standalone: false
})
export class EventDeleteDialogComponent {
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: Event
  ) {

  }
}
