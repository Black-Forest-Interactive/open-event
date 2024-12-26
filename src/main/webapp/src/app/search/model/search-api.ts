import {Page} from "../../shared/model/page";
import {AccountInfo} from "../../account/model/account-api";
import {Category} from "../../category/model/category-api";

export class EventSearchRequest {
  public constructor(
    public fullTextSearch: string,
    public from: string | undefined,
    public to: string | undefined,
    public ownEvents: boolean,
    public participatingEvents: boolean,
    public onlyAvailableEvents: boolean
  ) {
  }
}


export interface EventSearchResponse {
  result: Page<EventSearchEntry>
}


export interface EventSearchEntry {
  // event data
  id: string,
  start: string,
  finish: string,

  title: string,
  shortText: string,
  longText: string,
  published: boolean,

  owner: AccountInfo,

  // location data
  hasLocation: boolean,
  street: string,
  streetNumber: string,
  zip: string,
  city: string,
  country: string,

  lat: number,
  lon: number,

  // registration data
  hasSpaceLeft: boolean,
  maxGuestAmount: number,
  amountAccepted: number,
  amountOnWaitingList: number,
  remainingSpace: number,

  // user specific flags
  ownEvent: boolean,
  participatingEvent: boolean,

  // categories
  categories: string[],
  tags: string[],
}

export class AccountSearchRequest {
  public constructor(
    public fullTextSearch: string,
  ) {
  }
}

export interface AccountSearchResponse {
  result: Page<AccountSearchEntry>
}

export interface AccountSearchEntry {
  id: number,
  name: string,
  email: string,
  phone: string,
  mobile: string,
  firstName: string,
  lastName: string,
}

export class CategorySearchRequest {
  public constructor(
    public fullTextSearch: string,
  ) {
  }
}

export interface CategorySearchResponse {
  result: Page<Category>
}
