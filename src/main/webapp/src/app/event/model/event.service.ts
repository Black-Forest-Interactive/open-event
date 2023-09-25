import {Injectable} from '@angular/core';
import {BaseService} from "../../shared/model/base-service";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {Page} from "../../shared/model/page";
import {Event, EventChangeRequest, EventInfo, EventStats, PatchRequest} from "./event-api";
import {LocationChangeRequest} from "../../location/model/location-api";
import {RegistrationChangeRequest} from "../../registration/model/registration-api";
import * as moment from "moment/moment";
import {Moment} from "moment/moment";

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

  searchEvents(query: string, page: number, size: number): Observable<Page<EventInfo>> {
    let params = new HttpParams().set("query", query)
    return this.getPaged('search', page, size, params)
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

  createBackofficeEvent(accountId: number, request: EventChangeRequest): Observable<Event> {
    return this.post('' + accountId, request)
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

  buildIndex(): Observable<any> {
    return this.post('search', {})
  }

  getStats(): Observable<EventStats[]> {
    return this.get('stats')
  }


  createRequest(value: any, endHidden: boolean): EventChangeRequest | undefined {
    let start = this.createDateTime(value.event.startTime, value.event.startDate);
    let end = endHidden ? this.createDateTime(value.event.endTime, value.event.startDate) : this.createDateTime(value.event.endTime, value.event.endDate);
    if (!start || !end) return undefined

    let location = new LocationChangeRequest(
      value.location.street,
      value.location.streetNumber,
      value.location.zip,
      value.location.city,
      value.location.country,
      value.location.additionalInfo,
      0.0,
      0.0,
      -1
    )
    let registration = new RegistrationChangeRequest(
      value.registration.maxGuestAmount,
      value.registration.interestedAllowed,
      value.registration.ticketsEnabled
    )


    return new EventChangeRequest(
      start.format("YYYY-MM-DD[T]HH:mm:ss"),
      end.format("YYYY-MM-DD[T]HH:mm:ss"),
      value.event.title,
      value.event.shortText,
      value.event.longText,
      value.event.imageUrl,
      value.event.iconUrl,
      value.registration.categories,
      location,
      registration,
      true
    )
  }

  private createDateTime(timeStr: string, date: any): Moment | undefined {
    let mDate = moment(date)
    let time = timeStr.split(":");
    if (time.length == 2 && mDate.isValid()) {
      mDate.hours(parseInt(time[0]));
      mDate.minutes(parseInt(time[1]));
      return mDate
    }
    return undefined;
  }
}
