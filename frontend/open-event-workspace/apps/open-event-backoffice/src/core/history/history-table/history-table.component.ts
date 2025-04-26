import {Component, input, output} from '@angular/core';
import {MatTableModule} from "@angular/material/table";
import {TranslatePipe} from "@ngx-translate/core";
import {DatePipe} from "@angular/common";
import {AccountDisplayNamePipe, HistoryEntry} from "@open-event-workspace/core";
import {MatPaginator, PageEvent} from "@angular/material/paginator";

@Component({
  selector: 'app-history-table',
  imports: [MatTableModule, TranslatePipe, DatePipe, AccountDisplayNamePipe, MatPaginator],
  templateUrl: './history-table.component.html',
  styleUrl: './history-table.component.scss'
})
export class HistoryTableComponent {

  data = input.required<HistoryEntry[]>()
  reloading = input.required<boolean>()
  pageNumber = input.required<number>()
  pageSize = input.required<number>()
  totalElements = input.required<number>()

  pageEvent = output<PageEvent>()
  displayedColumns: string[] = ['timestamp', 'actor', 'type', 'message', 'source', 'info']
}
