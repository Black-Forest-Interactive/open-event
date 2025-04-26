import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogActions, MatDialogClose, MatDialogContent, MatDialogTitle} from "@angular/material/dialog";
import {Address} from "@open-event-workspace/core";
import {TranslatePipe} from "@ngx-translate/core";
import {MatButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";

@Component({
  selector: 'app-address-delete-dialog',
  templateUrl: './address-delete-dialog.component.html',
  styleUrl: './address-delete-dialog.component.scss',
  imports: [
    MatDialogTitle,
    TranslatePipe,
    MatDialogActions,
    MatDialogContent,
    MatDialogClose,
    MatButton,
    MatIcon
  ],
  standalone: true
})
export class AddressDeleteDialogComponent {
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: Address
  ) {

  }
}
