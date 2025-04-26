import {AccountDetails, AccountInfo} from "../account/account.api";

export interface Participant {
  id: number,
  size: number,
  status: string,
  rank: number,
  waitingList: boolean,
  author: AccountInfo,
  timestamp: string,
}

export class ParticipantChangeRequest {
  constructor(
    public size: number,
    public status: string,
  ) {
  }
}

export interface ParticipantDetails {
  id: number,
  size: number,
  status: string,
  rank: number,
  waitingList: boolean,
  author: AccountDetails,
  timestamp: string,
}
