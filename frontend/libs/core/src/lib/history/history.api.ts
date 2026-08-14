import { Account } from '../account/account.api'
import { EventSearchEntry } from '../search/search.api'

export interface HistoryEntry {
  id: number
  eventId: number
  timestamp: string
  actor: Account
  type: string
  message: string
  source: string
  info: string
}

export interface HistoryEventInfo {
  event: EventSearchEntry
  entries: HistoryEntry[]
}
