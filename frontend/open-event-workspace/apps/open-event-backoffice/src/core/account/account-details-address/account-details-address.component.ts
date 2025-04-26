import {Component, computed, effect, input, resource, signal} from '@angular/core';
import {Account, Address} from "@open-event-workspace/core";
import {TranslatePipe} from "@ngx-translate/core";
import {toPromise} from "@open-event-workspace/shared";
import {AccountService} from "@open-event-workspace/backoffice";
import {MatTableModule} from "@angular/material/table";
import {MatPaginatorModule, PageEvent} from "@angular/material/paginator";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {MatDialog} from "@angular/material/dialog";
import {AddressChangeDialogComponent} from "../../address/address-change-dialog/address-change-dialog.component";
import {AddressDeleteDialogComponent} from "../../address/address-delete-dialog/address-delete-dialog.component";
import {AddressCreateDialogComponent} from "../../address/address-create-dialog/address-create-dialog.component";
import {HotToastService} from "@ngxpert/hot-toast";
import {BoardCardComponent, BoardCardToolbarActions} from "../../../shared/board-card/board-card.component";

@Component({
  selector: 'app-account-details-address',
  imports: [
    MatTableModule,
    MatPaginatorModule,
    MatIconModule,
    MatButtonModule,
    TranslatePipe,
    BoardCardComponent,
    BoardCardToolbarActions
  ],
  templateUrl: './account-details-address.component.html',
  styleUrl: './account-details-address.component.scss'
})
export class AccountDetailsAddressComponent {
  data = input.required<Account>()

  page = signal(0)
  size = signal(20)

  addressCriteria = computed(() => ({
    data: this.data(),
    page: this.page(),
    size: this.size(),
  }));

  addressResource = resource({
    request: this.addressCriteria,
    loader: (param) => {
      return toPromise(this.service.getAddress(param.request.data.id, param.request.page, param.request.size))
    }
  })

  result = computed(this.addressResource.value ?? undefined)

  address = computed(() => this.result()?.content ?? [])
  totalSize = computed(() => this.result()?.totalSize ?? 0)
  loading = this.addressResource.isLoading
  error = this.addressResource.error

  displayedColumns: string[] = ['street', 'streetNumber', 'zip', 'city', 'country', 'additionalInfo', 'cmd']

  constructor(private service: AccountService, private toast: HotToastService, private dialog: MatDialog) {
    effect(() => {
      this.handleError(this.error())
    });
  }

  handlePageChange($event: PageEvent) {
    this.page.set($event.pageIndex)
    this.size.set($event.pageSize)
  }

  createAddress() {
    this.dialog.open(AddressCreateDialogComponent, {data: this.data()}).afterClosed().subscribe(value => this.addressResource.reload())
  }

  editAddress(address: Address) {
    this.dialog.open(AddressChangeDialogComponent, {data: address}).afterClosed().subscribe(value => this.addressResource.reload())
  }

  deleteAddress(address: Address) {
    this.dialog.open(AddressDeleteDialogComponent, {data: address}).afterClosed().subscribe(value => this.addressResource.reload())
  }


  importAddress() {
    this.service.importAddress(this.data().id).subscribe({
      next: value => this.addressResource.reload(),
      error: e => this.handleError(e)
    })
  }

  private handleError(e: any) {
    if (e) this.toast.error(e)
  }
}
