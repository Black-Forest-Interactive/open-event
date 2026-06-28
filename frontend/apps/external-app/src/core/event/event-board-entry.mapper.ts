import { PublicEvent } from '@open-event/external'
import { EventBoardEntry } from '@open-event/ui'

export function toEventBoardEntry(event: PublicEvent): EventBoardEntry {
  return {
    id: event.key,
    link: `/event/${event.key}`,
    title: event.title,
    shortText: event.shortText,
    start: event.start,
    finish: event.finish,
    hasLocation: event.hasLocation,
    city: event.city,
    categories: event.categories,
    audiences: [],
    tags: event.tags,
    featured: false,
    isRegistered: false,
    maxGuestAmount: event.maxGuestAmount,
    remainingSpace: event.remainingSpace,
    hasSpaceLeft: event.hasSpaceLeft
  }
}
