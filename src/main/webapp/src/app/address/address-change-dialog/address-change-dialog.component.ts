import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Address, AddressChangeRequest} from "../model/address-api";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
    selector: 'app-address-change-dialog',
    templateUrl: './address-change-dialog.component.html',
    styleUrl: './address-change-dialog.component.scss',
    standalone: false
})
export class AddressChangeDialogComponent {

  fg: FormGroup

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: Address | undefined,
    public dialogRef: MatDialogRef<AddressChangeDialogComponent>,
    fb: FormBuilder
  ) {
    this.fg = fb.group({
      street: [data?.street ?? '', Validators.required],
      streetNumber: [data?.streetNumber ?? '', Validators.required],
      zip: [data?.zip ?? '', Validators.required],
      city: [data?.city ?? '', Validators.required],
      country: [data?.country ?? 'Deutschland', Validators.required],
      additionalInfo: [data?.additionalInfo ?? ''],
      lat: [data?.lat ?? ''],
      lon: [data?.lon ?? ''],
    });
  }

  submit() {
    if (!this.fg.valid) return
    let value = this.fg.value
    this.dialogRef.close(value as AddressChangeRequest)
  }
}
