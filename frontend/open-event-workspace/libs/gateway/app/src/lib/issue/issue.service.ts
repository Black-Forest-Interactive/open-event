import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {BaseIssueService, IssueChangeRequest} from "@open-event-workspace/core";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class IssueService extends BaseIssueService {


  constructor(http: HttpClient) {
    super(http, 'app/issue')
    this.retryCount = 0
  }

  override createIssue(request: IssueChangeRequest): Observable<any> {
    return this.post('', request)
  }
}
