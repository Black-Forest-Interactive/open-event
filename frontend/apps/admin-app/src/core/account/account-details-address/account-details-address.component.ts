import { Component, computed, effect, inject, input, resource, signal } from '@angular/core'
import { Account, Address } from '@open-event/core'
import { TranslatePipe } from '@ngx-translate/core'
import { toPromise } from '@open-event/shared'
import { AccountService } from '@open-event/admin'
import { MatTableModule } from '@angular/material/table'
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator'
import { MatIconModule } from '@angular/material/icon'
import { MatButtonModule } from '@angular/material/button'
import { MatDialog } from '@angular/material/dialog'
import { AddressChangeDialogComponent } from '../../address/address-change-dialog/address-change-dialog.component'
import { AddressDeleteDialogComponent } from '../../address/address-delete-dialog/address-delete-dialog.component'
import { AddressCreateDialogComponent } from '../../address/address-create-dialog/address-create-dialog.component'
import { HotToastService } from '@ngxpert/hot-toast'
import { BoardCardComponent, BoardCardToolbarActions } from '../../../shared/board-card/board-card.component'

@Component({
  selector: 'admin-account-details-address',
  imports: [MatTableModule, MatPaginatorModule, MatIconModule, MatButtonModule, TranslatePipe, BoardCardComponent, BoardCardToolbarActions],
  templateUrl: './account-details-address.component.html',
  styleUrl: './account-details-address.component.scss'
})
export class AccountDetailsAddressComponent {
  private service = inject(AccountService)
  private toast = inject(HotToastService)
  private dialog = inject(MatDialog)

  data = input.required<Account>()

  page = signal(0)
  size = signal(20)

  readonly addressCriteria = computed(() => ({
    data: this.data(),
    page: this.page(),
    size: this.size()
  }))

  private addressResource = resource({
    params: this.addressCriteria,
    loader: (param) => toPromise(this.service.getAddress(param.params.data.id, param.params.page, param.params.size), param.abortSignal)
  })

  readonly result = computed(this.addressResource.value ?? undefined)

  readonly address = computed(() => this.result()?.content ?? [])
  readonly totalSize = computed(() => this.result()?.totalSize ?? 0)
  readonly loading = this.addressResource.isLoading
  readonly error = this.addressResource.error

  displayedColumns: string[] = ['street', 'streetNumber', 'zip', 'city', 'country', 'additionalInfo', 'cmd']

  constructor() {
    effect(() => {
      this.handleError(this.error())
    })
  }

  handlePageChange($event: PageEvent) {
    this.page.set($event.pageIndex)
    this.size.set($event.pageSize)
  }

  createAddress() {
    this.dialog
      .open(AddressCreateDialogComponent, { data: this.data() })
      .afterClosed()
      .subscribe(() => this.addressResource.reload())
  }

  editAddress(address: Address) {
    this.dialog
      .open(AddressChangeDialogComponent, { data: address })
      .afterClosed()
      .subscribe(() => this.addressResource.reload())
  }

  deleteAddress(address: Address) {
    this.dialog
      .open(AddressDeleteDialogComponent, { data: address })
      .afterClosed()
      .subscribe(() => this.addressResource.reload())
  }

  importAddress() {
    this.service.importAddress(this.data().id).subscribe({
      next: () => this.addressResource.reload(),
      error: (e) => this.handleError(e)
    })
  }

  private handleError(e: any) {
    if (e) this.toast.error()
  }
}
