import {Component, Inject} from '@angular/core';
import {AccountService, AddressService, CategoryService, EventService} from "@open-event-workspace/backoffice";
import {MAT_DIALOG_DATA, MatDialogContent, MatDialogRef} from "@angular/material/dialog";
import {Address, AddressReadAPI, Category, CategoryReadAPI, Event, EventChangeComponent, EventChangeRequest, EventInfo, EventReadAPI} from "@open-event-workspace/core";
import {Observable} from "rxjs";
import {Page} from "@open-event-workspace/shared";
import {TranslatePipe} from "@ngx-translate/core";

@Component({
  selector: 'app-event-change-dialog',
  imports: [
    EventChangeComponent,
    MatDialogContent,
    TranslatePipe
  ],
  templateUrl: './event-change-dialog.component.html',
  styleUrl: './event-change-dialog.component.scss'
})
export class EventChangeDialogComponent implements AddressReadAPI, CategoryReadAPI, EventReadAPI {

  constructor(
    private service: EventService,
    private accountService: AccountService,
    private addressService: AddressService,
    private categoryService: CategoryService,
    public dialogRef: MatDialogRef<EventChangeDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Event
  ) {
  }

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
      next: val => this.dialogRef.close(true),
      error: err => this.dialogRef.close(true),
    })
  }
}
