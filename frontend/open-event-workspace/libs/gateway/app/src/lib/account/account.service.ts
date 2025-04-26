import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Account, AccountChangeRequest, AccountValidationResult, Preferences, PreferencesChangeRequest, Profile, ProfileChangeRequest} from "@open-event-workspace/core";
import {Observable} from "rxjs";
import {BaseService} from "@open-event-workspace/shared";

@Injectable({
  providedIn: 'root'
})
export class AccountService extends BaseService {

  constructor(http: HttpClient) {
    super(http, 'app/account')
    this.retryCount = 1
  }

  getAccount(): Observable<Account> {
    return this.get('')
  }

  updateAccount(request: AccountChangeRequest): Observable<Account> {
    return this.put('', request)
  }

  validate(lang: string): Observable<AccountValidationResult> {
    let params = new HttpParams()
      .set("lang", lang)
    return this.get('validate', params)
  }

  getProfile(): Observable<Profile> {
    return this.get('profile')
  }

  updateProfile(request: ProfileChangeRequest): Observable<Profile> {
    return this.put('profile', request)
  }

  getPreferences(): Observable<Preferences> {
    return this.get('preferences')
  }

  updatePreferences(request: PreferencesChangeRequest): Observable<Preferences> {
    return this.put('preferences', request)
  }
}
