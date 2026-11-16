import { Injectable } from '@angular/core'
import { BaseService, Page } from '@open-event/shared'
import { AccountInfo } from '@open-event/core'
import { Observable } from 'rxjs'
import { NotificationSetting, NotificationSettingPatch } from './notification.model'

@Injectable({
  providedIn: 'root'
})
export class NotificationService extends BaseService {
  constructor() {
    super('backoffice/newsletter')
    this.retryCount = 0
  }

  getNewsletterSubscribers(page: number, size: number): Observable<Page<AccountInfo>> {
    return this.getPaged('subscriber', page, size)
  }

  getNewsletterSetting(): Observable<NotificationSetting | null> {
    return this.get('setting')
  }

  setNewsletterEnabled(id: number, enabled: boolean): Observable<NotificationSetting> {
    return this.put(`setting/${id}/enabled`, new NotificationSettingPatch(enabled))
  }
}
