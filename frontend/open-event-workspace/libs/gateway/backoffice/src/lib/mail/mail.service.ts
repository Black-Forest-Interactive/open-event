import {Injectable} from "@angular/core";
import {BaseService, Page} from "@open-event-workspace/shared";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {MailJob, MailJobHistoryEntry} from "@open-event-workspace/core";

@Injectable({
  providedIn: 'root'
})
export class MailService extends BaseService {

  constructor(http: HttpClient) {
    super(http, 'backoffice/mail')
    this.retryCount = 0
  }

  getJobs(page: number, size: number): Observable<Page<MailJob>> {
    return this.getPaged('', page, size)
  }

  getFailedJobs(page: number, size: number): Observable<Page<MailJob>> {
    return this.getPaged('failed', page, size)
  }

  getJobHistory(jobId: number, page: number, size: number): Observable<Page<MailJobHistoryEntry>> {
    return this.getPaged(jobId + '/history', page, size)
  }


}
