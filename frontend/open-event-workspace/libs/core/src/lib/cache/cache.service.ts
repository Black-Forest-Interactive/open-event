import {Injectable} from '@angular/core';
import {BaseService} from "@open-event-workspace/shared";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {CacheInfo} from "./cache.api";

/**
 * @deprecated use gateway instead
 */
@Injectable({
  providedIn: 'root'
})
export class CacheService extends BaseService {
  constructor(http: HttpClient) {
    super(http, 'cache')
    this.retryCount = 1
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
