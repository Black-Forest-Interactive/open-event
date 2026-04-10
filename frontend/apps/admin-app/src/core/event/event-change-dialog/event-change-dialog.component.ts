import { Component, inject } from '@angular/core'
import { AccountService, AddressService, CategoryService, EventService } from '@open-event/admin'
import { MAT_DIALOG_DATA, MatDialogContent, MatDialogRef } from '@angular/material/dialog'
import { Address, AddressReadAPI, Category, CategoryReadAPI, Event, EventChangeComponent, EventChangeRequest, EventInfo, EventReadAPI } from '@open-event/core'
import { Observable } from 'rxjs'
import { Page } from '@open-event/shared'
import { TranslatePipe } from '@ngx-translate/core'

@Component({
  selector: 'admin-event-change-dialog',
  imports: [EventChangeComponent, MatDialogContent, TranslatePipe],
  templateUrl: './event-change-dialog.component.html',
  styleUrl: './event-change-dialog.component.scss'
})
export class EventChangeDialogComponent implements AddressReadAPI, CategoryReadAPI, EventReadAPI {
  dialogRef = inject<MatDialogRef<EventChangeDialogComponent>>(MatDialogRef)
  data = inject<Event>(MAT_DIALOG_DATA)
  private service = inject(EventService)
  private accountService = inject(AccountService)
  private addressService = inject(AddressService)
  private categoryService = inject(CategoryService)

  getAllAddresses(page: number, size: number): Observable<Page<Address>> {
    return this.accountService.getAddress(this.data.owner.id, page, size)
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
    return this.service.getEvent(id)
  }

  getEventInfo(id: number): Observable<EventInfo> {
    return this.service.getEventInfo(id)
  }

  onCancelClick(): void {
    this.dialogRef.close(false)
  }

  handleRequest(request: EventChangeRequest | undefined) {
    if (!request) return this.onCancelClick()
    this.service.updateEvent(this.data.id, request).subscribe({
      next: () => this.dialogRef.close(true),
      error: () => this.dialogRef.close(true)
    })
  }
}
