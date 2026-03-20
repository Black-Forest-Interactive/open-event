import { Injectable } from '@angular/core'
import { BaseService } from '@open-event-workspace/shared'
import { Observable } from 'rxjs'
import { TextResponse } from '@open-event-workspace/core'

@Injectable({
  providedIn: 'root'
})
export class SettingsService extends BaseService {
  constructor() {
    super('app/settings')
    this.retryCount = 1
  }

  getTitle(): Observable<TextResponse> {
    return this.get('title')
  }

  getTerms(): Observable<TextResponse> {
    return this.get('terms')
  }
}
