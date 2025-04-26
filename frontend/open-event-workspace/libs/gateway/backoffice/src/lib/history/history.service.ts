import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {HistoryEntry, HistoryEventInfo} from "@open-event-workspace/core";
import {Observable} from "rxjs";
import {BaseService, Page} from "@open-event-workspace/shared";

@Injectable({
  providedIn: 'root'
})
export class HistoryService extends BaseService {

  constructor(http: HttpClient) {
    super(http, 'backoffice/history')
    this.retryCount = 0
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
