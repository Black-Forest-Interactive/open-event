import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {Address} from "../model/address-api";

@Component({
    selector: 'app-address-delete-dialog',
    templateUrl: './address-delete-dialog.component.html',
    styleUrl: './address-delete-dialog.component.scss',
    standalone: false
})
export class AddressDeleteDialogComponent {
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: Address
  ) {

  }
}
