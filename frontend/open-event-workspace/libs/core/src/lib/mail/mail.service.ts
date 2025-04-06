import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {MailJob, MailJobHistoryEntry} from "./mail-api";
import {Observable} from "rxjs";
import {BaseService, Page} from "@open-event-workspace/shared";


@Injectable({
  providedIn: 'root'
})
export class MailService extends BaseService {

  constructor(http: HttpClient) {
    super(http, 'mail')
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

  reRunJob(jobId: number): Observable<any> {
    return this.post(jobId + '', {})
  }


}
