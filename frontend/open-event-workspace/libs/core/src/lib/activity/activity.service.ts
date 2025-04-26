import {Injectable} from '@angular/core';
import {BaseService, Page} from "@open-event-workspace/shared";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Activity, ActivityInfo} from "./activity.api";

/**
 * @deprecated use gateway instead
 */
@Injectable({
  providedIn: 'root'
})
export class ActivityService extends BaseService {
  constructor(http: HttpClient) {
    super(http, 'activity')
    this.retryCount = 0
  }

  getRecentActivityInfos(page: number, size: number): Observable<Page<ActivityInfo>> {
    return this.getPaged('recent', page, size)
  }

  getUnreadActivityInfos(): Observable<ActivityInfo[]> {
    return this.get('unread')
  }

  markRead(id: number): Observable<Activity> {
    return this.put(id + '/read', {})
  }


  markAllRead(): Observable<ActivityInfo[]> {
    return this.put('read', {})
  }
}
