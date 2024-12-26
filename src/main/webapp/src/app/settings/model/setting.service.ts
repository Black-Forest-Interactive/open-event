import {Injectable} from '@angular/core';
import { HttpClient } from "@angular/common/http";
import {Observable} from "rxjs";
import {Setting, SettingChangeRequest, TextResponse} from "./settings-api";
import {BaseService} from "../../shared/model/base-service";
import {Page} from "../../shared/model/page";

@Injectable({
  providedIn: 'root'
})
export class SettingService extends BaseService {

  constructor(http: HttpClient) {
    super(http, 'settings')
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

  getTitle(): Observable<TextResponse> {
    return this.get('title')
  }

}
