import { Component, inject } from '@angular/core'
import { AddressService } from '@open-event/admin'
import { MAT_DIALOG_DATA, MatDialogActions, MatDialogContent, MatDialogRef, MatDialogTitle } from '@angular/material/dialog'
import { Address, AddressChangeComponent, AddressChangeRequest } from '@open-event/core'
import { MatButton } from '@angular/material/button'
import { MatIcon } from '@angular/material/icon'
import { TranslatePipe } from '@ngx-translate/core'

@Component({
  selector: 'admin-address-change-dialog',
  imports: [MatButton, MatDialogActions, MatDialogContent, MatDialogTitle, MatIcon, TranslatePipe, AddressChangeComponent],
  templateUrl: './address-change-dialog.component.html',
  styleUrl: './address-change-dialog.component.scss'
})
export class AddressChangeDialogComponent {
  dialogRef = inject<MatDialogRef<AddressChangeDialogComponent>>(MatDialogRef)
  data = inject<Address | undefined>(MAT_DIALOG_DATA)
  private service = inject(AddressService)

  onCancelClick(): void {
    this.dialogRef.close(false)
  }

  handleRequest(request: AddressChangeRequest) {
    const observable = this.data ? this.service.updateAddress(this.data.id, request) : this.service.createAddress(request)
    observable.subscribe({
      next: () => this.dialogRef.close(true),
      error: () => this.dialogRef.close(true)
    })
  }
}
