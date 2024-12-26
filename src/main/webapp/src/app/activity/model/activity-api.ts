import {AccountInfo} from "../../account/model/account-api";

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
