import {Component, input, output} from '@angular/core';
import {AccountDisplayNamePipe, EventPublishedIconComponent, EventSearchEntry} from "@open-event-workspace/core";
import {MatPaginatorModule, PageEvent} from "@angular/material/paginator";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {MatTableModule} from "@angular/material/table";
import {TranslatePipe} from "@ngx-translate/core";
import {RouterLink} from "@angular/router";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-event-table',
  imports: [
    MatIconModule,
    MatButtonModule,
    MatPaginatorModule,
    MatTableModule,
    TranslatePipe,
    RouterLink,
    AccountDisplayNamePipe,
    DatePipe,
    EventPublishedIconComponent
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
  editEvent = output<EventSearchEntry>()
  deleteEvent = output<EventSearchEntry>()
  publishEvent = output<EventSearchEntry>()
  displayedColumns: string[] = ['id', 'owner', 'title', 'date', 'published', 'participants', 'tags', 'cmd']
}
