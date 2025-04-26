import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogActions, MatDialogClose, MatDialogContent, MatDialogRef, MatDialogTitle} from "@angular/material/dialog";
import {ReactiveFormsModule} from "@angular/forms";
import {Address, AddressChangeComponent, AddressChangeRequest} from "@open-event-workspace/core";
import {TranslatePipe} from "@ngx-translate/core";
import {MatIcon} from "@angular/material/icon";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-address-change-dialog',
  templateUrl: './address-change-dialog.component.html',
  styleUrl: './address-change-dialog.component.scss',
  imports: [
    MatDialogTitle,
    TranslatePipe,
    ReactiveFormsModule,
    MatDialogContent,
    MatDialogActions,
    MatIcon,
    MatButton,
    MatDialogClose,
    AddressChangeComponent
  ],
  standalone: true
})
export class AddressChangeDialogComponent {

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: Address | undefined,
    public dialogRef: MatDialogRef<AddressChangeDialogComponent>,
  ) {

  }

  handleRequest(event: AddressChangeRequest) {
    this.dialogRef.close(event)
  }
}
