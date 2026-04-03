import { Component, inject } from "@angular/core";
import { TranslatePipe } from "@ngx-translate/core";

import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from "@angular/forms";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { MatDialogModule, MatDialogRef } from "@angular/material/dialog";
import { MatDatepickerModule } from "@angular/material/datepicker";
import { ActivityCleanupRequest } from "@open-event/core";
import { MatIconModule } from "@angular/material/icon";
import { MatButtonModule } from "@angular/material/button";
import { DateTime } from "luxon";

@Component({
  selector: "admin-activity-cleanup-dialog",
  imports: [
    TranslatePipe,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatIconModule,
    MatDialogModule,
    MatButtonModule,
  ],
  templateUrl: "./activity-cleanup-dialog.component.html",
  styleUrl: "./activity-cleanup-dialog.component.scss",
})
export class ActivityCleanupDialogComponent {
  dialogRef = inject<MatDialogRef<ActivityCleanupDialogComponent>>(MatDialogRef);

  fg: FormGroup;

  constructor() {
    const fb = inject(FormBuilder);

    this.fg = fb.group({
      timestamp: [
        DateTime.now().minus({ days: 90 }).toJSDate(),
        Validators.required,
      ],
    });
  }

  submit() {
    if (!this.fg.valid) return;
    let value = this.fg.value;
    let timestamp = DateTime.fromJSDate(value.timestamp);
    let request: ActivityCleanupRequest = new ActivityCleanupRequest(
      timestamp.toFormat("yyyy-MM-dd'T'00:00:00"),
    );
    this.dialogRef.close(request);
  }
}
