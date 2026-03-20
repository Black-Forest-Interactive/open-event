import { Injectable } from '@angular/core'
import { FeedbackChangeRequest } from '@open-event-workspace/core'
import { Observable } from 'rxjs'
import { BaseService } from '@open-event-workspace/shared'

@Injectable({
  providedIn: 'root'
})
export class FeedbackService extends BaseService {
  constructor() {
    super('app/feedback')
    this.retryCount = 0
  }

  createFeedback(request: FeedbackChangeRequest): Observable<any> {
    return this.post('', request)
  }
}
