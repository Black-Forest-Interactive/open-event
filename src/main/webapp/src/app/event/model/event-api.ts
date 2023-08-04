import {Account} from "../../account/model/account-api";
import {Location, LocationChangeRequest} from "../../location/model/location-api";
import {Registration, RegistrationChangeRequest} from "../../registration/model/registration-api";

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
  registration: Registration | undefined
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
    public location: LocationChangeRequest,
    public registration: RegistrationChangeRequest
  ) {
  }
}


export class PatchRequest<T> {
  constructor(
    public value: T
  ) {

  }

}
