import { Component, input, output } from '@angular/core'
import { Audience } from '@open-event/core'
import { MatPaginator, PageEvent } from '@angular/material/paginator'
import { MatCell, MatCellDef, MatColumnDef, MatHeaderCell, MatHeaderCellDef, MatHeaderRow, MatHeaderRowDef, MatRow, MatRowDef, MatTable } from '@angular/material/table'
import { MatIcon } from '@angular/material/icon'
import { MatIconAnchor, MatIconButton } from '@angular/material/button'
import { TranslatePipe } from '@ngx-translate/core'

@Component({
  selector: 'admin-audience-table',
  imports: [
    MatCell,
    MatCellDef,
    MatColumnDef,
    MatHeaderCell,
    MatHeaderRow,
    MatHeaderRowDef,
    MatIcon,
    MatIconButton,
    MatPaginator,
    MatRow,
    MatRowDef,
    MatTable,
    TranslatePipe,
    MatIconAnchor,
    MatHeaderCellDef
  ],
  templateUrl: './audience-table.component.html',
  styleUrl: './audience-table.component.scss'
})
export class AudienceTableComponent {
  data = input.required<Audience[]>()
  reloading = input.required<boolean>()
  pageNumber = input.required<number>()
  pageSize = input.required<number>()
  totalElements = input.required<number>()

  pageEvent = output<PageEvent>()
  editEvent = output<Audience>()
  deleteEvent = output<Audience>()
  displayedColumns: string[] = ['name', 'cmd']
}
