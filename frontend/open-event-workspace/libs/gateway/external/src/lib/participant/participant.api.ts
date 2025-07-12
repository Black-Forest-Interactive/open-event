import {PublicAccount} from "@open-event-workspace/external";

export class ExternalParticipantAddRequest {
  constructor(
    public firstName: string,
    public lastName: string,
    public email: string,
    public phone: string,
    public mobile: string,
    public size: number
  ) {
  }
}

export class ExternalParticipantChangeRequest {
  constructor(
    public size: number
  ) {
  }
}

export class ExternalParticipantConfirmRequest {
  constructor(
    public code: string
  ) {
  }
}

export interface ExternalParticipantChangeResponse {
  status: string
}

export interface ExternalParticipantConfirmResponse {
  participant: ExternalParticipant | undefined,
  status: string
}

export interface ExternalParticipant {
  size: number,
  status: string,
  rank: number,
  waitingList: boolean,
  author: PublicAccount,
  timestamp: string,
}
