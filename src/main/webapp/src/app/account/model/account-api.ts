import {Profile, ProfileChangeRequest} from "../../profile/model/profile-api";

export interface Account {
  id: number,
  externalId: string | undefined,
  name: string,
  iconUrl: string,

  registrationDate: string,
  lastLoginDate: string | undefined,

  serviceAccount: boolean,
  idpLinked: boolean,
}

export interface AccountInfo {
  id: number,
  name: string,
  iconUrl: string,
  email: string,
  firstName: string,
  lastName: string,
}

export interface AccountValidationResult {
  created: boolean,
  account: Account,
  profile: Profile,
  info: AccountInfo
}

export class AccountChangeRequest {
  constructor(
    public name: string,
    public iconUrl: string,
    public externalId: string | undefined
  ) {
  }
}

export class AccountSetupRequest {
  constructor(
    public account: AccountChangeRequest,
    public profile: ProfileChangeRequest
  ) {
  }
}


export interface AccountDetails {
  id: number,
  name: string,
  iconUrl: string,
  email: string,
  phone: string,
  mobile: string,
  firstName: string,
  lastName: string,
}
