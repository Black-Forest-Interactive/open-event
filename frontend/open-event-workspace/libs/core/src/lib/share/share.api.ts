import {AccountInfo} from "../account/account.api";
import {Category} from "../category/category-api";
import {Participant} from "../participant/participant.api";

export interface Share {
  id: string,
  eventId: number,
  published: boolean,
  url: string,

  owner: AccountInfo,
  created: string,
  changed: string | null
}

export class ShareChangeRequest {
  constructor(
    public eventId: number,
    public published: boolean,
  ) {
  }
}

export interface ShareInfo {
  event: SharedEvent,
  location: SharedLocation | null,
  registration: SharedRegistration | null,
  categories: Category[],
}

export interface SharedEvent {
  id: number,

  owner: SharedAccount,

  start: string,
  finish: string,

  title: string,
  shortText: string,
  longText: string,
  imageUrl: string,
  iconUrl: string,

  published: boolean,
}

export interface SharedAccount {
  id: number,
  name: string,
}


export interface SharedLocation {
  street: string,
  streetNumber: string,
  zip: string,
  city: string,
  country: string,
  additionalInfo: string,

  lat: number,
  lon: number,

  size: number
}

export interface SharedRegistration {
  maxGuestAmount: number,
  interestedAllowed: Boolean,
  ticketsEnabled: Boolean,

  participants: SharedParticipant[],
}

export interface SharedParticipant {
  size: number,
  status: string,
  rank: number,
  waitingList: boolean,
  timestamp: string,
}

export interface SharedParticipateResponse {
  registration: SharedRegistration
  participant: Participant | null
  status: string
  created: boolean
}
