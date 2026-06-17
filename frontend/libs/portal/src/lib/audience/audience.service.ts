import { Injectable } from '@angular/core'
import { BaseService, Page } from '@open-event/shared'
import { Observable } from 'rxjs'
import { Audience } from '@open-event/core'

@Injectable({
  providedIn: 'root'
})
export class AudienceService extends BaseService {
  constructor() {
    super('app/audience')
    this.retryCount = 1
  }

  getAudiences(page: number, size: number): Observable<Page<Audience>> {
    return this.getPaged('', page, size)
  }
}
