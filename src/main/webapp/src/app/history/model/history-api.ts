import {Account} from "../../account/model/account-api";
import {Event} from "../../event/model/event-api";

export interface HistoryEntry {
  id: number,
  eventId: number,
  timestamp: string,
  actor: Account,
  type: string,
  message: string,
  source: string,
  info: string
}

export interface HistoryEventInfo {
  event: Event
  entries: HistoryEntry[]
}
