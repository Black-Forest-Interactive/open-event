import {Component, EventEmitter} from '@angular/core';
import {SettingService} from "../model/setting.service";
import {HotToastService} from "@ngneat/hot-toast";
import {Setting} from "../model/settings-api";
import {debounceTime, distinctUntilChanged} from "rxjs";
import {Page} from "../../shared/model/page";
import {PageEvent} from "@angular/material/paginator";

@Component({
  selector: 'app-settings-board',
  templateUrl: './settings-board.component.html',
  styleUrls: ['./settings-board.component.scss']
})
export class SettingsBoardComponent {
  reloading: boolean = false
  pageNumber = 0
  pageSize = 10
  totalElements = 0

  data: Setting[] = []

  displayedColumns: string[] = ['id', 'value', 'type', 'cmd']

  keyUp: EventEmitter<string> = new EventEmitter<string>()


  constructor(
    private service: SettingService,
    private toastService: HotToastService
  ) {
  }

  ngOnInit(): void {
    this.loadPage(this.pageNumber)

    this.keyUp.pipe(
      debounceTime(500),
      distinctUntilChanged()
    ).subscribe(data => this.search(data))
  }

  private loadPage(number: number) {
    if (this.reloading) return
    this.reloading = true

    this.service.getAllSetting(number, this.pageSize).subscribe(p => this.handleData(p))
  }

  private handleData(p: Page<Setting>) {
    this.data = p.content

    this.totalElements = p.totalSize
    this.pageNumber = p.pageable.number
    this.pageSize = p.pageable.size
    this.reloading = false
  }

  search(query: string) {
    this.toastService.error("Not implemented yet to search for '" + query + "'")
  }

  handlePageChange(event: PageEvent) {
    if (this.reloading) return
    this.pageSize = event.pageSize
    this.loadPage(event.pageIndex)
  }
}
