import {PublicAccount} from "../account/account.api";

export interface PublicEvent {
  key: string,

  start: string,
  finish: string,

  title: string,
  shortText: string,
  longText: string,

  owner: PublicAccount,

  // location data
  hasLocation: boolean,
  zip: string,
  city: string,
  country: string,

  // registration data
  hasSpaceLeft: boolean,
  maxGuestAmount: number,
  amountAccepted: number,
  amountOnWaitingList: number,
  remainingSpace: number,

  // categories
  categories: string[],
  tags: string[],
}
