import {Injectable} from "@angular/core";
import {BaseService, Page, PatchRequest} from "@open-event-workspace/shared";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {Category, Event, EventChangeRequest, EventInfo, EventReadAPI, EventSearchRequest, EventSearchResponse, EventStats, HistoryEntry, Location, Registration} from "@open-event-workspace/core";


@Injectable({
  providedIn: 'root'
})
export class EventService extends BaseService implements EventReadAPI {

  constructor(http: HttpClient) {
    super(http, 'backoffice/event')
    this.retryCount = 0
  }

  getEvent(id: number): Observable<Event> {
    return this.get('' + id)
  }

  getEventInfo(id: number): Observable<EventInfo> {
    return this.get('' + id + '/info')
  }

  search(request: EventSearchRequest, page: number, size: number): Observable<EventSearchResponse> {
    let params = new HttpParams()
      .set("page", page)
      .set("size", size)
    return this.post('search', request, params)
  }

  getEventLocation(id: number): Observable<Location> {
    return this.get('' + id + '/location')
  }

  getEventRegistration(id: number): Observable<Registration> {
    return this.get('' + id + '/registration')
  }

  getEventCategories(id: number): Observable<Category[]> {
    return this.get('' + id + '/category')
  }

  getEventHistory(id: number, page: number, size: number): Observable<Page<HistoryEntry>> {
    let params = new HttpParams()
      .set("page", page)
      .set("size", size)
    return this.get('' + id + '/history', params)
  }

  getEventStats(id: number): Observable<EventStats> {
    return this.get('stats')
  }

  updateEvent(id: number, request: EventChangeRequest): Observable<Event> {
    return this.put('' + id, request)
  }

  deleteEvent(id: number): Observable<Event> {
    return this.delete('' + id)
  }

  publish(id: number): Observable<Event> {
    return this.put('' + id + '/published', new PatchRequest(true))
  }


}
