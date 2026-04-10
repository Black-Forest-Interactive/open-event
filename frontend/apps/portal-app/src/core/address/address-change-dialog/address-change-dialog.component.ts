import { Component, inject } from '@angular/core'
import { MAT_DIALOG_DATA, MatDialogActions, MatDialogClose, MatDialogContent, MatDialogRef, MatDialogTitle } from '@angular/material/dialog'
import { ReactiveFormsModule } from '@angular/forms'
import { Address, AddressChangeComponent, AddressChangeRequest } from '@open-event/core'
import { TranslatePipe } from '@ngx-translate/core'
import { MatIcon } from '@angular/material/icon'
import { MatButton } from '@angular/material/button'

@Component({
  selector: 'portal-address-change-dialog',
  templateUrl: './address-change-dialog.component.html',
  styleUrl: './address-change-dialog.component.scss',
  imports: [MatDialogTitle, TranslatePipe, ReactiveFormsModule, MatDialogContent, MatDialogActions, MatIcon, MatButton, MatDialogClose, AddressChangeComponent],
  standalone: true
})
export class AddressChangeDialogComponent {
  data = inject<Address | undefined>(MAT_DIALOG_DATA)
  dialogRef = inject<MatDialogRef<AddressChangeDialogComponent>>(MatDialogRef)

  handleRequest(event: AddressChangeRequest) {
    this.dialogRef.close(event)
  }
}
