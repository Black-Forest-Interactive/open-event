import { Injectable } from '@angular/core'
import { BaseService, Page } from '@open-event/shared'
import { Observable } from 'rxjs'
import { NotificationSettingPatch, NotificationType } from './notification.model'

@Injectable({
  providedIn: 'root'
})
export class NotificationTypeService extends BaseService {
  constructor() {
    super('backoffice/notification')
    this.retryCount = 0
  }

  getTypes(page: number, size: number): Observable<Page<NotificationType>> {
    return this.getPaged('type', page, size)
  }

  setTypeEnabled(id: number, enabled: boolean): Observable<NotificationType> {
    return this.put(`type/${id}/enabled`, new NotificationSettingPatch(enabled))
  }
}
