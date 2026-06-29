import { EventSearchEntry } from '@open-event/core'
import { EventBoardEntry } from '@open-event/ui'

export function toEventBoardEntry(entry: EventSearchEntry, basePath = '/event/details'): EventBoardEntry {
  return {
    id: String(entry.id),
    link: `${basePath}/${entry.id}`,
    title: entry.title,
    shortText: entry.shortText,
    start: entry.start,
    finish: entry.finish,
    hasLocation: entry.hasLocation,
    city: entry.city,
    categories: entry.categories,
    audiences: entry.audiences,
    tags: entry.tags,
    featured: entry.featured,
    isRegistered: entry.participatingEvent,
    maxGuestAmount: entry.maxGuestAmount,
    remainingSpace: entry.remainingSpace,
    hasSpaceLeft: entry.hasSpaceLeft
  }
}
