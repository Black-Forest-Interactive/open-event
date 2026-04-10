import { Injectable } from '@angular/core'
import { BaseService, Page } from '@open-event/shared'
import { Observable } from 'rxjs'
import { MailJob, MailJobHistoryEntry } from '@open-event/core'

@Injectable({
  providedIn: 'root'
})
export class MailService extends BaseService {
  constructor() {
    super('backoffice/mail')
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

  retryFailedJob(jobId: number): Observable<any> {
    return this.put(jobId + '/retry', {})
  }
}
