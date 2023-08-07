import {Participant} from "../../participant/model/participant-api";

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

export class ParticipateRequest {
  constructor(
    public size: number
  ) {
  }
}

export interface ParticipateResponse {
  registration: Registration,
  participants: Participant[],
  status: string
}
