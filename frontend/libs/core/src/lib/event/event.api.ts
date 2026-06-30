import { RegistrationChangeRequest, RegistrationInfo } from '../registration'
import { Category } from '../category'
import { Audience } from '../audience'
import { Location, LocationChangeRequest } from '../location'
import { Observable } from 'rxjs'
import { ShareInfo } from '../share'
import { AccountInfo } from '../account'

export interface Event {
  id: number
  owner: AccountInfo
  start: string
  finish: string

  title: string
  shortText: string
  longText: string
  imageUrl: string
  iconUrl: string
  featured: boolean

  hasLocation: boolean
  hasRegistration: boolean
  published: boolean

  tags: string[]

  created: string
  changed: string | null
}

export interface EventInfo {
  event: Event
  location: Location | undefined
  registration: RegistrationInfo | undefined
  categories: Category[]
  audiences: Audience[]
  share: ShareInfo | undefined
  bookmarked: boolean
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
    public audienceIds: number[],
    public location: LocationChangeRequest,
    public registration: RegistrationChangeRequest,
    public published: boolean,
    public shared: boolean,
    public tags: string[]
  ) {}
}

export interface EventStats {
  event: Event
  isFull: boolean
  isEmpty: boolean
  participantsSize: number
  participantsAmount: number
  waitingListSize: number
  waitingListAmount: number
}

export interface EventReadAPI {
  getEvent(id: number): Observable<Event>

  getEventInfo(id: number): Observable<EventInfo>

  create(request: EventChangeRequest): Observable<Event>
}

export class EventUpdateTextRequest {
  constructor(public title: string, public shortText: string, public longText: string) {}
}
