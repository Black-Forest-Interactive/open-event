import { Component, inject } from '@angular/core'
import { Account, AddressChangeRequest } from '@open-event/core'
import { AddressChangeComponent } from '@open-event/ui'
import { MatButton } from '@angular/material/button'
import { MAT_DIALOG_DATA, MatDialogActions, MatDialogContent, MatDialogRef } from '@angular/material/dialog'
import { MatIcon } from '@angular/material/icon'
import { TranslatePipe } from '@ngx-translate/core'
import { AccountService } from '@open-event/admin'

@Component({
  selector: 'admin-address-create-dialog',
  imports: [AddressChangeComponent, MatButton, MatDialogActions, MatDialogContent, MatIcon, TranslatePipe],
  templateUrl: './address-create-dialog.component.html',
  styleUrl: './address-create-dialog.component.scss'
})
export class AddressCreateDialogComponent {
  dialogRef = inject<MatDialogRef<AddressCreateDialogComponent>>(MatDialogRef)
  data = inject<Account>(MAT_DIALOG_DATA)
  private service = inject(AccountService)

  onCancelClick(): void {
    this.dialogRef.close(false)
  }

  handleRequest(request: AddressChangeRequest) {
    this.service.createAddress(this.data.id, request).subscribe({
      next: () => this.dialogRef.close(true),
      error: () => this.dialogRef.close(true)
    })
  }
}
