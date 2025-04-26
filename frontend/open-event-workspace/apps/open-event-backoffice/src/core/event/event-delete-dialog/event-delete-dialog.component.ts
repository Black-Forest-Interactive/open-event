import {Component, Inject} from '@angular/core';
import {EventService} from "@open-event-workspace/backoffice";
import {MAT_DIALOG_DATA, MatDialogActions, MatDialogContent, MatDialogRef, MatDialogTitle} from "@angular/material/dialog";
import {AccountDisplayNamePipe, Event} from "@open-event-workspace/core";
import {MatButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {TranslatePipe} from "@ngx-translate/core";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-event-delete-dialog',
  imports: [
    MatButton,
    MatDialogActions,
    MatDialogContent,
    MatDialogTitle,
    MatIcon,
    TranslatePipe,
    AccountDisplayNamePipe,
    DatePipe
  ],
  templateUrl: './event-delete-dialog.component.html',
  styleUrl: './event-delete-dialog.component.scss'
})
export class EventDeleteDialogComponent {
  constructor(
    private service: EventService,
    public dialogRef: MatDialogRef<EventDeleteDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Event
  ) {

  }

  onNoClick(): void {
    this.dialogRef.close(false)
  }

  onYesClick() {
    this.service.deleteEvent(this.data.id).subscribe(
      {
        next: val => this.dialogRef.close(true),
        error: err => this.dialogRef.close(true),
      }
    )
  }
}
