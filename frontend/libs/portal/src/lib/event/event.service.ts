import { Injectable } from '@angular/core'
import { HttpParams, HttpResponse } from '@angular/common/http'
import { BaseService, PatchRequest } from '@open-event/shared'
import { Event, EventChangeRequest, EventInfo, EventSearchRequest, EventSearchResponse, EventUpdateTextRequest } from '@open-event/core'
import { Observable } from 'rxjs'

@Injectable({
  providedIn: 'root'
})
export class EventService extends BaseService {
  constructor() {
    super('app/event')
    this.retryCount = 1
  }

  search(request: EventSearchRequest, page: number, size: number): Observable<EventSearchResponse> {
    const params = new HttpParams().set('page', page).set('size', size)
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

  setShared(id: number, enabled: boolean): Observable<EventInfo> {
    return this.put('' + id + '/shared', new PatchRequest(enabled))
  }

  setBookmarked(id: number): Observable<EventInfo> {
    return this.put('' + id + '/bookmark', {})
  }

  clearBookmarked(id: number): Observable<EventInfo> {
    return this.delete('' + id + '/bookmark')
  }

  setTitle(id: number, value: string): Observable<Event> {
    return this.put('' + id + '/title', new PatchRequest(value))
  }

  setShortText(id: number, value: string): Observable<Event> {
    return this.put('' + id + '/shortText', new PatchRequest(value))
  }

  setLongText(id: number, value: string): Observable<Event> {
    return this.put('' + id + '/longText', new PatchRequest(value))
  }

  setTags(id: number, value: string[]): Observable<Event> {
    return this.put('' + id + '/tags', new PatchRequest(value))
  }

  setText(id: number, request: EventUpdateTextRequest): Observable<Event> {
    return this.put('' + id + '/text', request)
  }

  exportEvent(eventId: number): Observable<HttpResponse<Blob>> {
    return this.getBlob('event/' + eventId + '/pdf')
  }
}
