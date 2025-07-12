import {Component} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatDialogModule, MatDialogRef} from "@angular/material/dialog";
import {ExternalParticipantAddRequest} from "@open-event-workspace/external";
import {MatInputModule} from "@angular/material/input";
import {TranslateModule} from "@ngx-translate/core";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";

@Component({
  selector: 'app-request-participation-dialog',
  imports: [MatDialogModule, MatInputModule, MatButtonModule, MatIconModule, ReactiveFormsModule, TranslateModule],
  templateUrl: './request-participation-dialog.component.html',
  styleUrl: './request-participation-dialog.component.scss'
})
export class RequestParticipationDialogComponent {
  fg: FormGroup

  constructor(
    public dialogRef: MatDialogRef<RequestParticipationDialogComponent>,
    fb: FormBuilder
  ) {
    this.fg = fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', Validators.compose([Validators.required, Validators.email])],
      phone: [''],
      mobile: [''],
      size: [1, Validators.compose([Validators.required, Validators.min(1)])]
    });
  }

  submit() {
    if (!this.fg.valid) return
    let value = this.fg.value
    this.dialogRef.close(new ExternalParticipantAddRequest(value.firstName, value.lastName, value.email, value.phone, value.mobile, value.size))
  }
}
