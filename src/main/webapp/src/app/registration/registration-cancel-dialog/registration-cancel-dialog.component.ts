import {Component} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";

@Component({
    selector: 'app-registration-cancel-dialog',
    templateUrl: './registration-cancel-dialog.component.html',
    styleUrls: ['./registration-cancel-dialog.component.scss'],
    standalone: false
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
