import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {BaseService, PatchRequest} from "@open-event-workspace/shared";
import {Event, EventChangeRequest, EventInfo, EventSearchRequest, EventSearchResponse} from "@open-event-workspace/core";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class EventService extends BaseService {

  constructor(http: HttpClient) {
    super(http, 'app/event')
    this.retryCount = 1
  }

  search(request: EventSearchRequest, page: number, size: number): Observable<EventSearchResponse> {
    let params = new HttpParams()
      .set("page", page)
      .set("size", size)
    return this.post('search', request, params)
  }

  getEvent(id: number): Observable<Event> {
    return this.get('' + id)
  }

  getEventInfo(id: number): Observable<EventInfo> {
    return this.get('' + id + '/info')
  }

  create(request: EventChangeRequest): Observable<Event> {
    return this.post('', request)
  }

  update(id: number, request: EventChangeRequest): Observable<Event> {
    return this.put(id + '', request)
  }

  deleteEvent(id: number): Observable<Event> {
    return this.delete('' + id)
  }

  publish(id: number): Observable<Event> {
    return this.put('' + id + '/published', new PatchRequest(true))
  }
}
