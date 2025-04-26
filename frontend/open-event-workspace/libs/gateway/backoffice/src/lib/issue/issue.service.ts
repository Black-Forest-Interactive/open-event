import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Issue} from "@open-event-workspace/core";
import {Observable} from "rxjs";
import {BaseService, Page, PatchRequest} from "@open-event-workspace/shared";

@Injectable({
  providedIn: 'root'
})
export class IssueService extends BaseService {

  constructor(http: HttpClient) {
    super(http, 'backoffice/issue')
    this.retryCount = 0
  }

  getIssue(id: number): Observable<Issue> {
    return this.get('' + id)
  }

  getAllIssues(page: number, size: number): Observable<Page<Issue>> {
    return this.getPaged('', page, size)
  }

  getAllIssuesByAccount(accountId: number, page: number, size: number): Observable<Page<Issue>> {
    let params = new HttpParams()
      .set("page", page)
      .set("size", size)
    return this.get('account/' + accountId, params)
  }

  getUnresolvedIssuesByAccount(accountId: number, page: number, size: number): Observable<Page<Issue>> {
    let params = new HttpParams()
      .set("page", page)
      .set("size", size)
    return this.get('account/' + accountId + '/unresolved', params)
  }

  getUnresolvedIssues(page: number, size: number): Observable<Page<Issue>> {
    let params = new HttpParams()
      .set("page", page)
      .set("size", size)
    return this.get('unresolved', params)
  }

  updateStatus(id: number, status: string): Observable<Issue> {
    return this.put('' + id + '/status', new PatchRequest(status))
  }

}
