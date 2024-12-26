import {Injectable} from '@angular/core';
import {BaseService} from "../../shared/model/base-service";
import { HttpClient } from "@angular/common/http";
import {AppService} from "../../app.service";
import {Observable} from "rxjs";
import {Page} from "../../shared/model/page";
import {Activity, ActivityInfo} from "./activity-api";

@Injectable({
  providedIn: 'root'
})
export class ActivityService extends BaseService {
  constructor(http: HttpClient, private appService: AppService) {
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
