import {Component} from '@angular/core';
import {MatDialogActions, MatDialogClose, MatDialogContent, MatDialogRef, MatDialogTitle} from "@angular/material/dialog";
import {TranslatePipe} from "@ngx-translate/core";
import {MatButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";

@Component({
    selector: 'app-registration-cancel-dialog',
    templateUrl: './registration-cancel-dialog.component.html',
    styleUrls: ['./registration-cancel-dialog.component.scss'],
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
export class RegistrationCancelDialogComponent {

  constructor(
    public dialogRef: MatDialogRef<RegistrationCancelDialogComponent>,
  ) {
  }

  submit() {
    this.dialogRef.close(true)
  }
}
