import { Component, inject } from "@angular/core";
import { MatButton } from "@angular/material/button";
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle,
} from "@angular/material/dialog";
import { MatIcon } from "@angular/material/icon";
import { TranslatePipe } from "@ngx-translate/core";
import { AddressService } from "@open-event/admin";
import { Address } from "@open-event/core";

@Component({
  selector: "admin-address-delete-dialog",
  imports: [
    MatButton,
    MatDialogActions,
    MatDialogContent,
    MatDialogTitle,
    MatIcon,
    TranslatePipe,
  ],
  templateUrl: "./address-delete-dialog.component.html",
  styleUrl: "./address-delete-dialog.component.scss",
})
export class AddressDeleteDialogComponent {
  private service = inject(AddressService);
  dialogRef = inject<MatDialogRef<AddressDeleteDialogComponent>>(MatDialogRef);
  data = inject<Address>(MAT_DIALOG_DATA);


  onNoClick(): void {
    this.dialogRef.close(false);
  }

  onYesClick() {
    this.service.deleteAddress(this.data.id).subscribe({
      next: (val) => this.dialogRef.close(true),
      error: (err) => this.dialogRef.close(true),
    });
  }
}
