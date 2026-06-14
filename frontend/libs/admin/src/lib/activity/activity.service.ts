import { Injectable } from '@angular/core'
import { HttpParams } from '@angular/common/http'
import { Activity, ActivityCleanupRequest } from '@open-event/core'
import { Observable } from 'rxjs'
import { BaseService, Page } from '@open-event/shared'

@Injectable({
  providedIn: 'root'
})
export class ActivityService extends BaseService {
  constructor() {
    super('backoffice/activity')
    this.retryCount = 0
  }

  getActivity(id: number): Observable<Activity> {
    return this.get('' + id)
  }

  getAllActivities(page: number, size: number): Observable<Page<Activity>> {
    return this.getPaged('', page, size)
  }

  cleanup(request: ActivityCleanupRequest): Observable<any> {
    return this.post('cleanup', request)
  }

  getRecentForAccount(accountId: number, page: number, size: number): Observable<Page<Activity>> {
    const params = new HttpParams().set('page', page).set('size', size)
    return this.get(accountId + '/recent', params)
  }
}
