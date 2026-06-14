import { Injectable } from '@angular/core'
import { Feedback } from '@open-event/core'
import { Observable } from 'rxjs'
import { BaseService, Page } from '@open-event/shared'

@Injectable({
  providedIn: 'root'
})
export class FeedbackService extends BaseService {
  constructor() {
    super('backoffice/feedback')
    this.retryCount = 0
  }

  getFeedback(id: number): Observable<Feedback> {
    return this.get('' + id)
  }

  getAllFeedbacks(page: number, size: number): Observable<Page<Feedback>> {
    return this.getPaged('', page, size)
  }
}
