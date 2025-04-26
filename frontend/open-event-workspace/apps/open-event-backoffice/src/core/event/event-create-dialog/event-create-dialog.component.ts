import {Component, inject} from '@angular/core';
import {AccountService, AddressService, CategoryService, EventService} from "@open-event-workspace/backoffice";
import {MAT_DIALOG_DATA, MatDialogContent, MatDialogRef} from "@angular/material/dialog";
import {Account, AccountSearchEntry, Address, AddressReadAPI, Category, CategoryReadAPI, Event, EventChangeComponent, EventChangeRequest, EventInfo, EventReadAPI} from "@open-event-workspace/core";
import {TranslatePipe} from "@ngx-translate/core";
import {Observable} from "rxjs";
import {Page} from "@open-event-workspace/shared";

@Component({
  selector: 'app-event-create-dialog',
  imports: [
    MatDialogContent,
    TranslatePipe,
    EventChangeComponent
  ],
  templateUrl: './event-create-dialog.component.html',
  styleUrl: './event-create-dialog.component.scss'
})
export class EventCreateDialogComponent implements AddressReadAPI, CategoryReadAPI, EventReadAPI {

  data: Account | AccountSearchEntry = inject(MAT_DIALOG_DATA)

  constructor(
    private service: AccountService,
    private eventService: EventService,
    private addressService: AddressService,
    private categoryService: CategoryService,
    public dialogRef: MatDialogRef<EventCreateDialogComponent>
  ) {
  }

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
      next: val => this.dialogRef.close(true),
      error: err => this.dialogRef.close(true),
    })
  }
}
