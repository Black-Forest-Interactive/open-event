import { Injectable } from '@angular/core'
import { HttpParams } from '@angular/common/http'
import { Observable } from 'rxjs'
import { BaseService, Page } from '@open-event/shared'
import { Audience, AudienceChangeRequest, AudienceReadAPI, AudienceSearchRequest, AudienceSearchResponse } from '@open-event/core'

@Injectable({
  providedIn: 'root'
})
export class AudienceService extends BaseService implements AudienceReadAPI {
  constructor() {
    super('backoffice/audience')
    this.retryCount = 0
  }

  getAudience(id: number): Observable<Audience> {
    return this.get('' + id)
  }

  getAllAudiences(page: number, size: number): Observable<Page<Audience>> {
    return this.getPaged('', page, size)
  }

  createAudience(request: AudienceChangeRequest): Observable<Audience> {
    return this.post('', request)
  }

  updateAudience(id: number, request: AudienceChangeRequest): Observable<Audience> {
    return this.put('' + id, request)
  }

  deleteAudience(id: number): Observable<Audience> {
    return this.delete('' + id)
  }

  search(request: AudienceSearchRequest, page: number, size: number): Observable<AudienceSearchResponse> {
    const params = new HttpParams().set('page', page).set('size', size)
    return this.post('search', request, params)
  }
}
