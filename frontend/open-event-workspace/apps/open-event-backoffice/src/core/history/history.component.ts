import {Component, EventEmitter} from '@angular/core';
import {DatePipe, NgClass} from '@angular/common';
import {HistoryEventInfo} from "@open-event-workspace/core";
import {Page} from "@open-event-workspace/shared";
import {MatCard} from "@angular/material/card";
import {MatTableModule} from "@angular/material/table";
import {MatDivider} from "@angular/material/divider";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {FormControl, FormGroup} from "@angular/forms";
import {HistoryService} from "@open-event-workspace/backoffice";
import {BoardComponent} from "../../shared/board/board.component";
import {HistoryTableComponent} from "./history-table/history-table.component";

@Component({
  selector: 'boffice-history',
  imports: [
    MatCard,
    NgClass,
    MatDivider,
    MatPaginator,
    MatTableModule,
    DatePipe,
    BoardComponent,
    HistoryTableComponent
  ],
  templateUrl: './history.component.html',
  styleUrl: './history.component.scss',
})
export class HistoryComponent {
  reloading: boolean = false
  pageNumber = 0
  pageSize = 25
  totalElements = 0

  displayedColumns: string[] = ['timestamp', 'actor', 'type', 'message', 'source', 'info']

  keyUp: EventEmitter<string> = new EventEmitter<string>()

  data: HistoryEventInfo[] = []
  selected: HistoryEventInfo | undefined

  range = new FormGroup({
    start: new FormControl<Date | null>(null),
    end: new FormControl<Date | null>(null),
  });

  constructor(
    private service: HistoryService
  ) {
  }

  ngOnInit() {
    this.reload()
  }


  reload() {
    this.loadPage(this.pageNumber)
  }

  handlePageChange(event: PageEvent) {
    if (this.reloading) return
    this.pageSize = event.pageSize
    this.loadPage(event.pageIndex)
  }


  private loadPage(number: number) {
    if (this.reloading) return
    this.reloading = true

    this.service.getAllHistoryEventInfos(number, this.pageSize).subscribe(p => this.handleData(p))
  }

  private handleData(page: Page<HistoryEventInfo>) {
    if (page == null) {
      this.data = []
      this.pageNumber = 0
      this.pageSize = 20
      this.totalElements = 0
      this.selected = undefined
    } else {
      this.data = page.content.filter(d => d != null)
      this.pageNumber = page.pageable.number
      this.pageSize = page.pageable.size
      this.totalElements = page.totalSize
      this.selected = this.data[0]
    }
    this.reloading = false
  }
}
