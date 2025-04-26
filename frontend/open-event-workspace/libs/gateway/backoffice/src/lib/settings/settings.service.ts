import {Injectable} from "@angular/core";
import {BaseService, Page} from "@open-event-workspace/shared";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Setting, SettingChangeRequest} from "@open-event-workspace/core";

@Injectable({
  providedIn: 'root'
})
export class SettingsService extends BaseService {

  constructor(http: HttpClient) {
    super(http, 'backoffice/settings')
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
