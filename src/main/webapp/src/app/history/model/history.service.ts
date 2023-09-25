import {Injectable} from '@angular/core';
import {BaseService} from "../../shared/model/base-service";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Page} from "../../shared/model/page";
import {HistoryEntry, HistoryEventInfo} from "./history-api";

@Injectable({
  providedIn: 'root'
})
export class HistoryService extends BaseService {
  constructor(http: HttpClient) {
    super(http, 'history')
    this.retryCount = 1
  }

  getAllHistoryEntries(page: number, size: number): Observable<Page<HistoryEntry>> {
    return this.getPaged('', page, size)
  }

  getAllHistoryEntriesForEvent(eventId: number, page: number, size: number): Observable<Page<HistoryEntry>> {
    return this.getPaged('for/event/' + eventId, page, size)
  }

  getAllHistoryEventInfos(page: number, size: number): Observable<Page<HistoryEventInfo>> {
    return this.getPaged('info', page, size)
  }
}
