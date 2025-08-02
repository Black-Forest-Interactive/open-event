import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";
import {BaseService} from "@open-event-workspace/shared";
import {EventSearchRequest} from "@open-event-workspace/core";


@Injectable({
  providedIn: 'root'
})
export class ExportService extends BaseService {
  constructor(http: HttpClient) {
    super(http, 'backoffice/export')
    this.retryCount = 0
  }

  exportEvents(request: EventSearchRequest): Observable<HttpResponse<Blob>> {
    return this.postBlob('event/pdf', request)
  }

  exportEventsToEmail(request: EventSearchRequest): Observable<any> {
    return this.post('event/pdf', request)
  }

  exportEvent(eventId: number): Observable<HttpResponse<Blob>> {
    return this.getBlob('event/' + eventId + '/pdf')
  }

  exportSummary(request: EventSearchRequest): Observable<HttpResponse<Blob>> {
    return this.postBlob('event/summary', request)
  }
}
