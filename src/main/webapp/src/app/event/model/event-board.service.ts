import {Injectable} from '@angular/core';
import {EventService} from "./event.service";
import {HotToastService} from "@ngneat/hot-toast";
import {EventInfo} from "./event-api";
import {Page} from "../../shared/model/page";
import {BehaviorSubject} from "rxjs";
import {PageEvent} from "@angular/material/paginator";

@Injectable({
  providedIn: 'root'
})
export class EventBoardService {

  reloading: BehaviorSubject<boolean> = new BehaviorSubject(false);
  searching: boolean = false
  pageSize: number = 20
  pageIndex: number = 0
  totalSize: number = 0
  infos: EventInfo[] = []
  query: string = ''

  constructor(private service: EventService, private toast: HotToastService) {
  }

  reload(page: number, size: number) {
    if (this.reloading.value) return
    this.reloading.next(true)
    if (this.query.length <= 0) {
      this.service.getEventInfos(page, size).subscribe({
        next: value => this.handleData(value),
        error: e => this.handleError(e)
      })
    } else {
      this.service.searchEvents(this.query, page, size).subscribe({
        next: value => this.handleData(value),
        error: e => this.handleError(e)
      })
    }
  }

  private handleData(value: Page<EventInfo>) {
    this.infos = value.content
    this.pageSize = value.pageable.size
    this.pageIndex = value.pageable.number
    this.totalSize = value.totalSize
    this.reloading.next(false)
    this.searching = false
  }

  private handleError(err: any) {
    // this.toast.error("Failed to load data")
    this.reloading.next(false)
  }

  search(query: string) {
    this.query = query
    if (this.searching) return
    this.reload(this.pageIndex, this.pageSize)
  }

  handlePageChange(event: PageEvent) {
    this.reload(event.pageIndex, event.pageSize)
  }
}
