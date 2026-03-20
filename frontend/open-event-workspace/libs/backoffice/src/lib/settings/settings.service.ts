import { Injectable } from '@angular/core'
import { Observable } from 'rxjs'
import { BaseService, Page } from '@open-event-workspace/shared'
import { Setting, SettingChangeRequest } from '@open-event-workspace/core'

@Injectable({
  providedIn: 'root'
})
export class SettingsService extends BaseService {
  constructor() {
    super('backoffice/settings')
    this.retryCount = 0
  }

  getAllSetting(page: number, size: number): Observable<Page<Setting>> {
    return this.getPaged('', page, size)
  }

  getSetting(id: number): Observable<Setting> {
    return this.get('' + id)
  }

  createSetting(request: SettingChangeRequest): Observable<Setting> {
    return this.post('', request)
  }

  updateSetting(id: number, request: SettingChangeRequest): Observable<Setting> {
    return this.put('' + id, request)
  }

  deleteSetting(id: number): Observable<Setting> {
    return this.delete('' + id)
  }
}
