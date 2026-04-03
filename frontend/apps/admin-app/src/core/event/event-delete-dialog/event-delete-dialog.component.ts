import { Component, inject } from "@angular/core";
import { EventService } from "@open-event/admin";
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle,
} from "@angular/material/dialog";
import { AccountDisplayNamePipe, Event } from "@open-event/core";
import { MatButton } from "@angular/material/button";
import { MatIcon } from "@angular/material/icon";
import { TranslatePipe } from "@ngx-translate/core";
import { DatePipe } from "@angular/common";

@Component({
  selector: "admin-event-delete-dialog",
  imports: [
    MatButton,
    MatDialogActions,
    MatDialogContent,
    MatDialogTitle,
    MatIcon,
    TranslatePipe,
    AccountDisplayNamePipe,
    DatePipe,
  ],
  templateUrl: "./event-delete-dialog.component.html",
  styleUrl: "./event-delete-dialog.component.scss",
})
export class EventDeleteDialogComponent {
  private service = inject(EventService);
  dialogRef = inject<MatDialogRef<EventDeleteDialogComponent>>(MatDialogRef);
  data = inject<Event>(MAT_DIALOG_DATA);


  onNoClick(): void {
    this.dialogRef.close(false);
  }

  onYesClick() {
    this.service.deleteEvent(this.data.id).subscribe({
      next: (val) => this.dialogRef.close(true),
      error: (err) => this.dialogRef.close(true),
    });
  }
}
