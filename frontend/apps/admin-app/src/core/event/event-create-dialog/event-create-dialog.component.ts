import { Component, inject } from '@angular/core'
import { AccountService, AddressService, CategoryService, EventService } from '@open-event/admin'
import { MAT_DIALOG_DATA, MatDialogContent, MatDialogRef } from '@angular/material/dialog'
import { Account, AccountSearchEntry, Address, AddressReadAPI, Category, CategoryReadAPI, Event, EventChangeComponent, EventChangeRequest, EventInfo, EventReadAPI } from '@open-event/core'
import { TranslatePipe } from '@ngx-translate/core'
import { Observable } from 'rxjs'
import { Page } from '@open-event/shared'

@Component({
  selector: 'admin-event-create-dialog',
  imports: [MatDialogContent, TranslatePipe, EventChangeComponent],
  templateUrl: './event-create-dialog.component.html',
  styleUrl: './event-create-dialog.component.scss'
})
export class EventCreateDialogComponent implements AddressReadAPI, CategoryReadAPI, EventReadAPI {
  private service = inject(AccountService)
  private eventService = inject(EventService)
  private addressService = inject(AddressService)
  private categoryService = inject(CategoryService)
  dialogRef = inject<MatDialogRef<EventCreateDialogComponent>>(MatDialogRef)

  data: Account | AccountSearchEntry = inject(MAT_DIALOG_DATA)

  getAllAddresses(page: number, size: number): Observable<Page<Address>> {
    return this.service.getAddress(this.data.id, page, size)
  }

  getAddress(id: number): Observable<Address> {
    return this.addressService.getAddress(id)
  }

  getAllCategories(page: number, size: number): Observable<Page<Category>> {
    return this.categoryService.getAllCategories(page, size)
  }

  getCategory(id: number): Observable<Category> {
    return this.categoryService.getCategory(id)
  }

  getEvent(id: number): Observable<Event> {
    return this.eventService.getEvent(id)
  }

  getEventInfo(id: number): Observable<EventInfo> {
    return this.eventService.getEventInfo(id)
  }

  onCancelClick(): void {
    this.dialogRef.close(false)
  }

  handleRequest(request: EventChangeRequest) {
    this.service.createEvent(this.data.id, request).subscribe({
      next: () => this.dialogRef.close(true),
      error: () => this.dialogRef.close(true)
    })
  }
}
