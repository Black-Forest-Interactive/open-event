import {Component} from '@angular/core';
import {Address} from "../model/address-api";
import {AddressService} from "../model/address.service";
import {MatDialog} from "@angular/material/dialog";
import {Page} from "../../shared/model/page";
import {AddressChangeDialogComponent} from "../address-change-dialog/address-change-dialog.component";
import {PageEvent} from "@angular/material/paginator";
import {AddressDeleteDialogComponent} from "../address-delete-dialog/address-delete-dialog.component";

@Component({
    selector: 'app-address-board',
    templateUrl: './address-board.component.html',
    styleUrl: './address-board.component.scss',
    standalone: false
})
export class AddressBoardComponent {

  address: Address[] = []
  reloading: boolean = false
  pageSize: number = 20
  pageIndex: number = 0
  totalSize: number = 0
  displayedColumns: string[] = ['street', 'streetNumber', 'zip', 'city', 'country', 'cmd']

  constructor(private service: AddressService, private dialog: MatDialog) {
  }

  ngOnInit() {
    this.reload()
  }

  private reload() {
    if (this.reloading) return
    this.reloading = true
    this.service.getAllForCurrentAccount(this.pageIndex, this.pageSize).subscribe({
      next: value => this.handleData(value),
      error: e => this.handleError(e)
    })
  }

  private handleData(value: Page<Address>) {
    this.address = value.content
    this.pageSize = value.pageable.size
    this.pageIndex = value.pageable.number
    this.totalSize = value.totalSize
    this.reloading = false
  }

  private handleError(e: any) {
    this.reloading = false
  }

  import() {
    if (this.reloading) return
    this.reloading = true
    this.service.importAddress().subscribe({
      next: value => this.handleData(value),
      error: e => this.handleError(e)
    })

  }

  create() {
    const dialogRef = this.dialog.open(AddressChangeDialogComponent, {
      data: undefined
    })
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.service.createAddress(result).subscribe(_ => this.reload())
      }
    })
  }

  edit(a: Address) {
    const dialogRef = this.dialog.open(AddressChangeDialogComponent, {
      data: a
    })
    dialogRef.afterClosed().subscribe(result => {
      if (result) this.service.updateAddress(a.id, result).subscribe(_ => this.reload())
    })
  }

  delete(a: Address) {
    const dialogRef = this.dialog.open(AddressDeleteDialogComponent, {
      data: a
    })
    dialogRef.afterClosed().subscribe(result => {
      if (result) this.service.deleteAddress(a.id).subscribe(_ => this.reload())
    })
  }

  handlePageChange(event: PageEvent) {

  }
}
