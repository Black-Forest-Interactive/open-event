import { Injectable } from '@angular/core'
import { BaseService } from '@open-event/shared'
import { Observable } from 'rxjs'
import { TextResponse, UrlResponse } from '@open-event/core'

@Injectable({ providedIn: 'root' })
export class SettingsService extends BaseService {
  constructor() {
    super('external/settings')
    this.retryCount = 1
  }
  getTitle(): Observable<TextResponse> {
    return this.get('title')
  }
  getPortalUrl(): Observable<UrlResponse> {
    return this.get('portal-url')
  }
}
