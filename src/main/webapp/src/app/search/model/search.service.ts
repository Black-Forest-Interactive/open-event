import {Injectable} from '@angular/core';
import {BaseService} from "../../shared/model/base-service";
import { HttpClient, HttpParams } from "@angular/common/http";
import {Observable} from "rxjs";
import {
  AccountSearchRequest,
  AccountSearchResponse,
  CategorySearchRequest,
  CategorySearchResponse,
  EventSearchRequest,
  EventSearchResponse
} from "./search-api";

@Injectable({
  providedIn: 'root'
})
export class SearchService extends BaseService {
  constructor(http: HttpClient) {
    super(http, 'search')
    this.retryCount = 0
  }

  setupEvents(): Observable<any> {
    return this.post('setup/event', {})
  }

  setupAccounts(): Observable<any> {
    return this.post('setup/account', {})
  }

  setupCategories(): Observable<any> {
    return this.post('setup/category', {})
  }

  searchEvents(request: EventSearchRequest, page: number, size: number): Observable<EventSearchResponse> {
    return this.search(request, 'event', page, size)
  }

  searchAccounts(request: AccountSearchRequest, page: number, size: number): Observable<AccountSearchResponse> {
    return this.search(request, 'account', page, size)
  }

  searchCategories(request: CategorySearchRequest, page: number, size: number): Observable<CategorySearchResponse> {
    return this.search(request, 'category', page, size)
  }

  private search<R, S>(request: R, suffix: string, page: number, size: number): Observable<S> {
    let params = new HttpParams()
      .set("page", page)
      .set("size", size)
    return this.post(suffix, request, params)
  }
}
