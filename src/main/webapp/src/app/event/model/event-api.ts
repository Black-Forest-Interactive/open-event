import {Account} from "../../account/model/account-api";
import {Location, LocationChangeRequest} from "../../location/model/location-api";
import {RegistrationChangeRequest, RegistrationInfo} from "../../registration/model/registration-api";
import {Category} from "../../category/model/category-api";

export interface Event {
  id: number,
  owner: Account,
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
    public published: boolean
  ) {
  }
}


export class PatchRequest<T> {
  constructor(
    public value: T
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
