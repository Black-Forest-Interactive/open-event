import {Component} from '@angular/core';
import {MatDialogModule, MatDialogRef} from "@angular/material/dialog";
import {MatInputModule} from "@angular/material/input";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {TranslateModule} from "@ngx-translate/core";
import {ExternalParticipantConfirmRequest} from "@open-event-workspace/external";

@Component({
  selector: 'app-confirm-participation-dialog',
  imports: [MatDialogModule, MatInputModule, MatButtonModule, MatIconModule, ReactiveFormsModule, TranslateModule],
  templateUrl: './confirm-participation-dialog.component.html',
  styleUrl: './confirm-participation-dialog.component.scss'
})
export class ConfirmParticipationDialogComponent {
  fg: FormGroup

  constructor(
    public dialogRef: MatDialogRef<ConfirmParticipationDialogComponent>,
    fb: FormBuilder
  ) {
    this.fg = fb.group({
      code: ['', Validators.required]
    });
  }

  submit() {
    if (!this.fg.valid) return
    let value = this.fg.value
    this.dialogRef.close(new ExternalParticipantConfirmRequest(value.code))
  }
}
