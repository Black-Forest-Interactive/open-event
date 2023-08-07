import {Account} from "../../account/model/account-api";

export interface Participant {
  id: number,
  size: number,
  status: string,
  rank: number,
  waitingList: boolean,
  author: Account,
  timestamp: string,
}

export class ParticipantChangeRequest {
  constructor(
    public size: number,
    public status: string,
  ) {
  }
}
