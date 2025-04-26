import {Component, Inject} from '@angular/core';
import {AddressService} from "@open-event-workspace/backoffice";
import {MAT_DIALOG_DATA, MatDialogActions, MatDialogContent, MatDialogRef, MatDialogTitle} from "@angular/material/dialog";
import {Address, AddressChangeComponent, AddressChangeRequest} from "@open-event-workspace/core";
import {MatButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {TranslatePipe} from "@ngx-translate/core";

@Component({
  selector: 'app-address-change-dialog',
  imports: [
    MatButton,
    MatDialogActions,
    MatDialogContent,
    MatDialogTitle,
    MatIcon,
    TranslatePipe,
    AddressChangeComponent
  ],
  templateUrl: './address-change-dialog.component.html',
  styleUrl: './address-change-dialog.component.scss'
})
export class AddressChangeDialogComponent {

  constructor(
    private service: AddressService,
    public dialogRef: MatDialogRef<AddressChangeDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Address | undefined
  ) {
  }

  onCancelClick(): void {
    this.dialogRef.close(false)
  }

  handleRequest(request: AddressChangeRequest) {
    let observable = (this.data) ? this.service.updateAddress(this.data.id, request) : this.service.createAddress(request)
    observable.subscribe({
      next: val => this.dialogRef.close(true),
      error: err => this.dialogRef.close(true),
    })
  }
}
