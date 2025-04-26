import {Component} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {AddressChangeDialogComponent} from "../address-change-dialog/address-change-dialog.component";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {AddressDeleteDialogComponent} from "../address-delete-dialog/address-delete-dialog.component";
import {Address} from "@open-event-workspace/core";
import {LoadingBarComponent, Page} from "@open-event-workspace/shared";
import {AppService} from "../../../shared/app.service";
import {MatCard} from "@angular/material/card";
import {TranslatePipe} from "@ngx-translate/core";
import {MatButton, MatIconButton} from "@angular/material/button";
import {MatDivider} from "@angular/material/divider";
import {MatTableModule} from "@angular/material/table";
import {MatIcon} from "@angular/material/icon";
import {AddressService} from "@open-event-workspace/app";

@Component({
  selector: 'app-address-board',
  templateUrl: './address-board.component.html',
  styleUrl: './address-board.component.scss',
  imports: [
    MatCard,
    TranslatePipe,
    MatButton,
    MatDivider,
    MatTableModule,
    MatIconButton,
    MatIcon,
    MatPaginator,
    LoadingBarComponent,
  ],
  standalone: true
})
export class AddressBoardComponent {

  address: Address[] = []
  reloading: boolean = false
  pageSize: number = 20
  pageIndex: number = 0
  totalSize: number = 0
  displayedColumns: string[] = ['street', 'streetNumber', 'zip', 'city', 'country', 'cmd']

  constructor(private service: AddressService, private appService: AppService, private dialog: MatDialog) {
  }

  ngOnInit() {
    this.reload()
  }

  private reload() {
    if (this.reloading) return
    let account = this.appService.account
    if (!account) return
    this.reloading = true
    this.service.getAddresses(this.pageIndex, this.pageSize).subscribe({
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
