import {Injectable} from '@angular/core';
import {BaseService} from "../../shared/model/base-service";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Page} from "../../shared/model/page";
import {Event, EventChangeRequest, EventInfo, PatchRequest} from "./event-api";

@Injectable({
  providedIn: 'root'
})
export class EventService extends BaseService {
  constructor(http: HttpClient) {
    super(http, 'event')
    this.retryCount = 1
  }

  getAllEvents(page: number, size: number): Observable<Page<Event>> {
    return this.getPaged('', page, size)
  }

  getEvent(id: number): Observable<Event> {
    return this.get('' + id)
  }

  getEventInfo(id: number): Observable<EventInfo> {
    return this.get('' + id + '/info')
  }

  getEventInfos(page: number, size: number): Observable<Page<EventInfo>> {
    return this.getPaged('info', page, size)
  }

  createEvent(request: EventChangeRequest): Observable<Event> {
    return this.post('', request)
  }

  updateEvent(id: number, request: EventChangeRequest): Observable<Event> {
    return this.put('' + id, request)
  }

  publish(id: number): Observable<Event> {
    return this.put('' + id + '/published', new PatchRequest(true))
  }

  deleteEvent(id: number): Observable<Event> {
    return this.delete('' + id)
  }
}
