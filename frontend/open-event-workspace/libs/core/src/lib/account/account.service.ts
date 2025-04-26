import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {Account, AccountChangeRequest, AccountInfo, AccountSetupRequest, AccountValidationResult} from "./account.api";
import {BaseService, Page} from "@open-event-workspace/shared";
import {Profile} from "../profile/profile.api";
import {Preferences} from "../preferences/preferences.api";

/**
 * @deprecated use gateway instead
 */
@Injectable({
  providedIn: 'root'
})
export class AccountService extends BaseService {
  constructor(http: HttpClient) {
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

  validate(lang: string): Observable<AccountValidationResult> {
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
