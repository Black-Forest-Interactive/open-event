import { Component, input, output } from '@angular/core'
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator'
import { MatTableModule } from '@angular/material/table'
import { MatIconModule } from '@angular/material/icon'
import { TranslatePipe } from '@ngx-translate/core'
import { AccountInfo } from '@open-event/core'

@Component({
  selector: 'admin-newsletter-table',
  imports: [MatIconModule, MatPaginatorModule, MatTableModule, TranslatePipe],
  templateUrl: './newsletter-table.component.html'
})
export class NewsletterTableComponent {
  data = input.required<AccountInfo[]>()
  reloading = input.required<boolean>()
  pageNumber = input.required<number>()
  pageSize = input.required<number>()
  totalElements = input.required<number>()

  pageEvent = output<PageEvent>()

  displayedColumns: string[] = ['name', 'firstName', 'lastName', 'email']
}
