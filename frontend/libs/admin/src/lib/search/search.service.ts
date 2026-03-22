import { Injectable } from '@angular/core'
import { BaseService } from '@open-event/shared'
import { Observable } from 'rxjs'
import { SearchOperatorInfo } from '@open-event/core'

@Injectable({
  providedIn: 'root'
})
export class SearchService extends BaseService {
  constructor() {
    super('backoffice/search')
    this.retryCount = 0
  }

  getInfo(key: string): Observable<SearchOperatorInfo> {
    return this.get('' + key)
  }

  getAllInfo(): Observable<SearchOperatorInfo[]> {
    return this.get('')
  }

  setup(key: string): Observable<SearchOperatorInfo> {
    return this.post('' + key, {})
  }
}
