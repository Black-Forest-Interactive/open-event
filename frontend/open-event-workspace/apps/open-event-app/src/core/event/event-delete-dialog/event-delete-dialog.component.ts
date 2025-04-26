import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogActions, MatDialogClose, MatDialogContent, MatDialogTitle} from "@angular/material/dialog";
import {Event} from "@open-event-workspace/core";
import {TranslatePipe} from "@ngx-translate/core";
import {MatButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";

@Component({
  selector: 'app-event-delete-dialog',
  templateUrl: './event-delete-dialog.component.html',
  styleUrls: ['./event-delete-dialog.component.scss'],
  imports: [
    MatDialogTitle,
    MatDialogContent,
    TranslatePipe,
    MatDialogActions,
    MatButton,
    MatIcon,
    MatDialogClose
  ],
  standalone: true
})
export class EventDeleteDialogComponent {
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: Event
  ) {

  }
}
