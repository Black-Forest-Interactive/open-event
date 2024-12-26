import {Injectable} from '@angular/core';
import {BaseService} from "../../shared/model/base-service";
import { HttpClient, HttpParams } from "@angular/common/http";
import {Observable} from "rxjs";
import {Account, AccountChangeRequest, AccountInfo, AccountSetupRequest, AccountValidationResult} from "./account-api";
import {Page} from "../../shared/model/page";
import {Profile} from "../../profile/model/profile-api";
import {Preferences} from "../../preferences/model/preferences-api";
import {TranslateService} from "@ngx-translate/core";

@Injectable({
  providedIn: 'root'
})
export class AccountService extends BaseService {
  constructor(http: HttpClient, private translate: TranslateService) {
    super(http, 'account')
    this.retryCount = 1
  }

  getAllAccounts(page: number, size: number): Observable<Page<Account>> {
    return this.getPaged('', page, size)
  }

  getAccount(id: number): Observable<Account> {
    return this.get('' + id)
  }

  createAccount(request: AccountChangeRequest): Observable<Account> {
    return this.post('', request)
  }

  validate(): Observable<AccountValidationResult> {
    let lang = this.translate.currentLang
    let params = new HttpParams()
      .set("lang", lang)
    return this.get('validate', params)
  }

  getProfile(): Observable<Profile> {
    return this.get('profile')
  }

  getPreferences(): Observable<Preferences> {
    return this.get('preferences')
  }

  setupAccount(request: AccountSetupRequest): Observable<AccountInfo> {
    return this.post('setup', request)
  }

  updateAccount(id: number, request: AccountSetupRequest): Observable<AccountInfo> {
    return this.put('setup/' + id, request)
  }

}
