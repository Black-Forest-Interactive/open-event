import {RegistrationChangeRequest, RegistrationInfo} from "../registration/registration.api";
import {Category} from "../category/category-api";
import {LocationChangeRequest} from "../location/location.api";
import {AccountInfo, Location} from "@open-event-workspace/core";
import {Observable} from "rxjs";

export interface Event {
  id: number,
  owner: AccountInfo,
  start: string,
  finish: string,

  title: string,
  shortText: string,
  longText: string,
  imageUrl: string,
  iconUrl: string,

  hasLocation: boolean,
  hasRegistration: boolean,
  published: boolean,

  tags: string[]
}

export interface EventInfo {
  event: Event,
  location: Location | undefined
  registration: RegistrationInfo | undefined,
  categories: Category[],
  canEdit: boolean
}


export class EventChangeRequest {
  constructor(
    public start: string,
    public finish: string,
    public title: string,
    public shortText: string,
    public longText: string,
    public imageUrl: string,
    public iconUrl: string,
    public categoryIds: number[],
    public location: LocationChangeRequest,
    public registration: RegistrationChangeRequest,
    public published: boolean,
    public tags: string[]
  ) {
  }
}


export interface EventStats {
  event: Event,
  isFull: boolean,
  isEmpty: boolean,
  participantsSize: number,
  participantsAmount: number,
  waitingListSize: number,
  waitingListAmount: number
}

export interface EventReadAPI {
  getEvent(id: number): Observable<Event>

  getEventInfo(id: number): Observable<EventInfo>
}
