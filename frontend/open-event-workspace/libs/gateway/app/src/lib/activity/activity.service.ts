import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ActivityInfo} from "@open-event-workspace/core";
import {Observable} from "rxjs";
import {BaseService, Page} from "@open-event-workspace/shared";

@Injectable({
  providedIn: 'root'
})
export class ActivityService extends BaseService {

  constructor(http: HttpClient) {
    super(http, 'app/activity')
    this.retryCount = 1
  }

  unreadAmount(): Observable<number> {
    return this.get('unread/amount')
  }

  unreadInfo(): Observable<ActivityInfo[]> {
    return this.get('unread/info')
  }

  getRecentActivityInfos(page: number, size: number): Observable<Page<ActivityInfo>> {
    return this.getPaged('recent', page, size)
  }

  markReadSingle(id: number): Observable<any> {
    return this.put('read/' + id, {})
  }

  markReadAll(): Observable<any> {
    return this.put('read', {})
  }

}
