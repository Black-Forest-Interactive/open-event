export interface Registration {
  id: number,
  maxGuestAmount: number,
  interestedAllowed: boolean,
  ticketsEnabled: boolean
}

export class RegistrationChangeRequest {
  constructor(
    public maxGuestAmount: number,
    public interestedAllowed: boolean,
    public ticketsEnabled: boolean
  ) {
  }
}
