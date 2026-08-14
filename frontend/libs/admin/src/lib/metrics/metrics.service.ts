import { Injectable } from '@angular/core'
import { EventMetricsDaily, EventMetricsWeekly } from '@open-event/core'
import { Observable } from 'rxjs'
import { BaseService } from '@open-event/shared'
import { HttpParams } from '@angular/common/http'

@Injectable({
  providedIn: 'root'
})
export class MetricsService extends BaseService {
  constructor() {
    super('backoffice/metrics')
    this.retryCount = 1
  }

  getDaily(from: string, to: string): Observable<EventMetricsDaily[]> {
    const params = new HttpParams().set('from', from).set('to', to)
    return this.get('daily', params)
  }

  getWeekly(from: string, to: string): Observable<EventMetricsWeekly[]> {
    const params = new HttpParams().set('from', from).set('to', to)
    return this.get('weekly', params)
  }

  recalculateWeekly(): Observable<void> {
    return this.post('weekly/recalculate', {})
  }
}
