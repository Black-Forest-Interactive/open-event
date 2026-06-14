import { Component, computed, inject, resource, signal } from '@angular/core'
import { LoadingBarComponent, toPromise } from '@open-event/shared'
import { MatButton, MatIconButton } from '@angular/material/button'
import { MatCard } from '@angular/material/card'
import { MatTableModule } from '@angular/material/table'
import { MatDivider } from '@angular/material/divider'
import { MatIcon } from '@angular/material/icon'
import { MatPaginator, PageEvent } from '@angular/material/paginator'
import { TranslatePipe } from '@ngx-translate/core'
import { AddressService } from '@open-event/portal'
import { MatDialog } from '@angular/material/dialog'
import { Address } from '@open-event/core'
import { AddressChangeDialogComponent } from './address-change-dialog/address-change-dialog.component'
import { AddressDeleteDialogComponent } from './address-delete-dialog/address-delete-dialog.component'

@Component({
  selector: 'portal-address',
  imports: [LoadingBarComponent, MatButton, MatCard, MatTableModule, MatDivider, MatIcon, MatIconButton, MatPaginator, TranslatePipe],
  templateUrl: './address.component.html',
  styleUrl: './address.component.scss'
})
export class AddressComponent {
  readonly displayedColumns = ['street', 'streetNumber', 'zip', 'city', 'country', 'cmd']
  private service = inject(AddressService)
  private dialog = inject(MatDialog)
  private page = signal(0)
  private size = signal(20)

  private criteria = computed(() => ({ page: this.page(), size: this.size() }))

  private addressResource = resource({
    params: this.criteria,
    loader: (p) => toPromise(this.service.getAddresses(p.params.page, p.params.size), p.abortSignal)
  })

  readonly address = computed(() => this.addressResource.value()?.content ?? [])
  readonly totalSize = computed(() => this.addressResource.value()?.totalSize ?? 0)
  readonly pageIndex = computed(() => this.addressResource.value()?.pageable.number ?? 0)
  readonly pageSize = computed(() => this.addressResource.value()?.pageable.size ?? 20)
  readonly loading = this.addressResource.isLoading

  import() {
    this.service.importAddress().subscribe({ next: () => this.addressResource.reload() })
  }

  create() {
    this.dialog
      .open(AddressChangeDialogComponent, { data: undefined })
      .afterClosed()
      .subscribe((result) => {
        if (result) this.service.createAddress(result).subscribe({ next: () => this.addressResource.reload() })
      })
  }

  edit(a: Address) {
    this.dialog
      .open(AddressChangeDialogComponent, { data: a })
      .afterClosed()
      .subscribe((result) => {
        if (result) this.service.updateAddress(a.id, result).subscribe({ next: () => this.addressResource.reload() })
      })
  }

  delete(a: Address) {
    this.dialog
      .open(AddressDeleteDialogComponent, { data: a })
      .afterClosed()
      .subscribe((result) => {
        if (result) this.service.deleteAddress(a.id).subscribe({ next: () => this.addressResource.reload() })
      })
  }

  handlePageChange(event: PageEvent) {
    this.page.set(event.pageIndex)
    this.size.set(event.pageSize)
  }
}
