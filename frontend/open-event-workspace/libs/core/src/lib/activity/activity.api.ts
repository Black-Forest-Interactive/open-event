import {AccountInfo} from "../account/account.api";

export interface ActivityInfo {
  activity: Activity,
  read: boolean
}

export interface Activity {
  id: number,
  title: string,
  actor: AccountInfo,
  source: string,
  sourceId: number,
  type: string,
  timestamp: string
}

export class ActivityCleanupRequest {
  constructor(
    public timestamp: string
  ) {
  }
}
