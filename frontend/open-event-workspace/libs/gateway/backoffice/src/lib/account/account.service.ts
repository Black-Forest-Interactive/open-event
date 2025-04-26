import {Injectable} from '@angular/core';
import {HttpClient, HttpParams, httpResource, HttpResourceRef} from "@angular/common/http";
import {
  Account,
  AccountChangeRequest,
  AccountInfo,
  AccountSearchRequest,
  AccountSearchResponse,
  AccountSetupRequest,
  AccountValidationResult,
  Address,
  AddressChangeRequest,
  Event,
  EventChangeRequest,
  Preferences,
  Profile
} from "@open-event-workspace/core";
import {Observable} from "rxjs";
import {BaseService, Page} from "@open-event-workspace/shared";

@Injectable({
  providedIn: 'root'
})
export class AccountService extends BaseService {

  constructor(http: HttpClient) {
    super(http, 'backoffice/account')
    this.retryCount = 0
  }

  validate(lang: string): Observable<AccountValidationResult> {
    let params = new HttpParams()
      .set("lang", lang)
    return this.get('validate', params)
  }

  getAccount(id: number): Observable<Account> {
    return this.get(id + '')
  }

  getProfile(id: number): Observable<Profile> {
    return this.get(id + '/profile')
  }

  getPreferences(id: number): Observable<Preferences> {
    return this.get(id + '/preferences')
  }

  getAddress(id: number, page: number, size: number): Observable<Page<Address>> {
    let params = new HttpParams()
      .set("page", page)
      .set("size", size)
    return this.get(id + '/address', params)
  }

  createAddress(id: number, request: AddressChangeRequest): Observable<Address> {
    return this.post(id + '/address', request)
  }

  importAddress(id: number): Observable<Page<Address>> {
    return this.post(id + '/address/import', {})
  }

  getEvents(id: number, page: number, size: number): Observable<Page<Event>> {
    let params = new HttpParams()
      .set("page", page)
      .set("size", size)
    return this.get(id + '/event', params)
  }

  createEvent(id: number, request: EventChangeRequest): Observable<Event> {
    return this.post(id + '/event', request)
  }

  search(request: AccountSearchRequest, page: number, size: number): Observable<AccountSearchResponse> {
    let params = new HttpParams()
      .set("page", page)
      .set("size", size)
    return this.post('search', request, params)
  }

  createAccount(request: AccountChangeRequest): Observable<Account> {
    return this.post('', request)
  }

  updateAccount(id: number, request: AccountChangeRequest): Observable<Account> {
    return this.put(id + '', request)
  }

  deleteAccount(id: number): Observable<Account> {
    return this.delete(id + '')
  }


  setupAccount(request: AccountSetupRequest): Observable<AccountInfo> {
    return this.post('setup', request)
  }

  updateSetupAccount(id: number, request: AccountSetupRequest): Observable<AccountInfo> {
    return this.put('setup/' + id, request)
  }
}
