export interface Account {
  id: number,
  externalId: string,
  name: string,
  firstName: string,
  lastName: string,
  email: string,
  iconUrl: string,
  serviceAccount: boolean,
}

export interface AccountValidationResult {
  created: boolean,
  account: Account
}

export class AccountChangeRequest {
  constructor(
    public name: string,
    public firstName: string,
    public lastName: string,
    public email: string,
    public iconUrl: string,
  ) {
  }
}


