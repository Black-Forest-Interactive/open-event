import {Component, EventEmitter} from '@angular/core';
import {HistoryService} from "../model/history.service";
import {Page} from "../../shared/model/page";
import {PageEvent} from "@angular/material/paginator";
import {HistoryEventInfo} from "../model/history-api";
import {FormControl, FormGroup} from '@angular/forms';

@Component({
    selector: 'app-history-board',
    templateUrl: './history-board.component.html',
    styleUrls: ['./history-board.component.scss'],
    standalone: false
})
export class HistoryBoardComponent {
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
