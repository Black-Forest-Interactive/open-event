import { Injectable } from '@angular/core'
import { CacheInfo } from '@open-event-workspace/core'
import { Observable } from 'rxjs'
import { BaseService } from '@open-event-workspace/shared'

@Injectable({
  providedIn: 'root'
})
export class CacheService extends BaseService {
  constructor() {
    super('backoffice/cache')
    this.retryCount = 0
  }

  getCache(key: string): Observable<CacheInfo> {
    return this.get(key)
  }

  getAllCaches(): Observable<CacheInfo[]> {
    return this.get('')
  }

  resetCache(key: string): Observable<CacheInfo> {
    return this.delete(key)
  }

  resetAllCaches(): Observable<CacheInfo[]> {
    return this.delete('')
  }
}
