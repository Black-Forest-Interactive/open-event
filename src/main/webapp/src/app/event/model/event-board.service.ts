import {Injectable} from '@angular/core';
import {EventService} from "./event.service";
import {HotToastService} from "@ngneat/hot-toast";
import {EventInfo} from "./event-api";
import {Page} from "../../shared/model/page";

@Injectable({
  providedIn: 'root'
})
export class EventBoardService {

  reloading: boolean = false
  pageSize: number = 20
  pageIndex: number = 0
  totalSize: number = 0
  infos: EventInfo[] = []

  constructor(private service: EventService, private toast: HotToastService) {
  }

  reload() {
    if (this.reloading) return
    this.reloading = true
    this.service.getEventInfos(this.pageIndex, this.pageSize).subscribe({
      next: value => this.handleData(value),
      error: e => this.handleError(e)
    })
  }

  private handleData(value: Page<EventInfo>) {
    this.infos = value.content
    this.pageSize = value.pageable.size
    this.pageIndex = value.pageable.number
    this.totalSize = value.totalSize
    this.reloading = false
  }

  private handleError(err: any) {
    // this.toast.error("Failed to load data")
    this.reloading = false
  }
}
