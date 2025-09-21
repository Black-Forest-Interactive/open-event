import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Feedback} from "@open-event-workspace/core";
import {Observable} from "rxjs";
import {BaseService, Page} from "@open-event-workspace/shared";

@Injectable({
  providedIn: 'root'
})
export class FeedbackService extends BaseService {

  constructor(http: HttpClient) {
    super(http, 'backoffice/feedback')
    this.retryCount = 0
  }

  getFeedback(id: number): Observable<Feedback> {
    return this.get('' + id)
  }

  getAllFeedbacks(page: number, size: number): Observable<Page<Feedback>> {
    return this.getPaged('', page, size)
  }

}
