import { Component, input, output } from '@angular/core'
import { AccountDisplayNamePipe, EventSearchEntry } from '@open-event/core'
import { EventPublishedIconComponent } from '@open-event/ui'
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator'
import { MatIconModule } from '@angular/material/icon'
import { MatButtonModule } from '@angular/material/button'
import { MatTableModule } from '@angular/material/table'
import { TranslatePipe } from '@ngx-translate/core'
import { RouterLink } from '@angular/router'
import { DatePipe } from '@angular/common'
import { MatSortModule, Sort } from '@angular/material/sort'
import { ExportEventButtonComponent } from '../../export/export-event-button/export-event-button.component'

@Component({
  selector: 'admin-event-table',
  imports: [
    MatIconModule,
    MatButtonModule,
    MatPaginatorModule,
    MatTableModule,
    MatSortModule,
    TranslatePipe,
    RouterLink,
    AccountDisplayNamePipe,
    DatePipe,
    EventPublishedIconComponent,
    ExportEventButtonComponent
  ],
  templateUrl: './event-table.component.html',
  styleUrl: './event-table.component.scss'
})
export class EventTableComponent {
  data = input.required<EventSearchEntry[]>()
  reloading = input.required<boolean>()
  pageNumber = input.required<number>()
  pageSize = input.required<number>()
  totalElements = input.required<number>()

  pageEvent = output<PageEvent>()
  sortEvent = output<Sort>()
  editEvent = output<EventSearchEntry>()
  deleteEvent = output<EventSearchEntry>()
  exportEvent = output<EventSearchEntry>()
  publishEvent = output<EventSearchEntry>()

  displayedColumns: string[] = ['id', 'owner', 'title', 'date', 'published', 'updated', 'participants', 'tags', 'cmd']
}
