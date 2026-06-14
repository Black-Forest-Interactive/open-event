import { Component, inject } from '@angular/core'
import { Account, AccountSearchEntry, EventChangeRequest } from '@open-event/core'
import { AccountService, AddressService, CategoryService, EventService } from '@open-event/admin'
import { MAT_DIALOG_DATA, MatDialogContent, MatDialogRef } from '@angular/material/dialog'
import { TranslatePipe } from '@ngx-translate/core'
import { EventCreateComponent } from '@open-event/ui'

@Component({
  selector: 'admin-event-create-dialog',
  imports: [MatDialogContent, TranslatePipe, EventCreateComponent],
  templateUrl: './event-create-dialog.component.html',
  styleUrl: './event-create-dialog.component.scss'
})
export class EventCreateDialogComponent {
  dialogRef = inject<MatDialogRef<EventCreateDialogComponent>>(MatDialogRef)
  data: Account | AccountSearchEntry = inject(MAT_DIALOG_DATA)
  private accountService = inject(AccountService)
  private eventService = inject(EventService)
  private addressService = inject(AddressService)
  private categoryService = inject(CategoryService)

  addressReadAPI = {
    getAllAddresses: (page: number, size: number) => this.accountService.getAddress(this.data.id, page, size),
    getAddress: (id: number) => this.addressService.getAddress(id)
  }

  categoryReadAPI = {
    getAllCategories: (page: number, size: number) => this.categoryService.getAllCategories(page, size),
    getCategory: (id: number) => this.categoryService.getCategory(id)
  }

  eventReadAPI = {
    getEvent: (id: number) => this.eventService.getEvent(id),
    getEventInfo: (id: number) => this.eventService.getEventInfo(id),
    create: (request: EventChangeRequest) => this.accountService.createEvent(this.data.id, request)
  }

  onCancelClick(): void {
    this.dialogRef.close(false)
  }

  onCreated(): void {
    this.dialogRef.close(true)
  }
}
