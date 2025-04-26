import {Component, Inject} from '@angular/core';
import {Account, AddressChangeComponent, AddressChangeRequest} from "@open-event-workspace/core";
import {MatButton} from "@angular/material/button";
import {MAT_DIALOG_DATA, MatDialogActions, MatDialogContent, MatDialogRef} from "@angular/material/dialog";
import {MatIcon} from "@angular/material/icon";
import {TranslatePipe} from "@ngx-translate/core";
import {AccountService} from "@open-event-workspace/backoffice";

@Component({
  selector: 'app-address-create-dialog',
  imports: [
    AddressChangeComponent,
    MatButton,
    MatDialogActions,
    MatDialogContent,
    MatIcon,
    TranslatePipe
  ],
  templateUrl: './address-create-dialog.component.html',
  styleUrl: './address-create-dialog.component.scss'
})
export class AddressCreateDialogComponent {

  constructor(
    private service: AccountService,
    public dialogRef: MatDialogRef<AddressCreateDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Account
  ) {
  }

  onCancelClick(): void {
    this.dialogRef.close(false)
  }

  handleRequest(request: AddressChangeRequest) {
    this.service.createAddress(this.data.id, request).subscribe({
      next: val => this.dialogRef.close(true),
      error: err => this.dialogRef.close(true),
    })
  }
}
