import { EventSearchEntry, EventSearchRequest } from '../search'

export type FinderAnswer = 'YES' | 'NO' | 'MAYBE'

export interface Finder {
  id: number
  ownerId: number
  name: string
  request: EventSearchRequest
  timestamp: string
}

export interface FinderInfo {
  id: number
  name: string
  request: EventSearchRequest
  timestamp: string
  countOpen: number
  countYes: number
  countNo: number
  countMaybe: number
}

export interface FinderPreview {
  total: number
}

export interface FinderEntry {
  finderId: number
  eventId: number
  answer: FinderAnswer
  timestamp: string
}

export interface FinderEntryInfo {
  entry: EventSearchEntry
  answer: FinderAnswer
  timestamp: string
}

export class FinderChangeRequest {
  constructor(
    public name: string,
    public request: EventSearchRequest
  ) {}
}

export class FinderAnswerRequest {
  constructor(public answer: FinderAnswer) {}
}
