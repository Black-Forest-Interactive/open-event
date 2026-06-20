import { Component, inject, signal } from '@angular/core'
import { MAT_DIALOG_DATA, MatDialogActions, MatDialogClose, MatDialogContent, MatDialogRef, MatDialogTitle } from '@angular/material/dialog'
import { ReactiveFormsModule } from '@angular/forms'
import { Address, AddressChangeRequest } from '@open-event/core'
import { AddressChangeComponent } from '@open-event/ui'
import { TranslatePipe } from '@ngx-translate/core'
import { MatIcon } from '@angular/material/icon'
import { MatButton } from '@angular/material/button'
import { MatSlideToggle } from '@angular/material/slide-toggle'
import { FormsModule } from '@angular/forms'

@Component({
  selector: 'portal-address-change-dialog',
  templateUrl: './address-change-dialog.component.html',
  styleUrl: './address-change-dialog.component.scss',
  imports: [MatDialogTitle, TranslatePipe, ReactiveFormsModule, FormsModule, MatDialogContent, MatDialogActions, MatIcon, MatButton, MatDialogClose, AddressChangeComponent, MatSlideToggle],
  standalone: true
})
export class AddressChangeDialogComponent {
  data = inject<Address | undefined>(MAT_DIALOG_DATA)
  dialogRef = inject<MatDialogRef<AddressChangeDialogComponent>>(MatDialogRef)

  readonly makeDefault = signal(this.data?.standard ?? false)

  handleRequest(request: AddressChangeRequest) {
    this.dialogRef.close({ request, makeDefault: this.makeDefault() })
  }
}
