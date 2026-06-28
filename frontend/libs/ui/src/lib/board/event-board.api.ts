export interface EventBoardEntry {
  id: string
  link: string
  title: string
  shortText: string
  start: string
  finish: string
  hasLocation: boolean
  city: string
  categories: string[]
  audiences: string[]
  tags: string[]
  featured: boolean
  isRegistered: boolean
  maxGuestAmount: number
  remainingSpace: number
  hasSpaceLeft: boolean
}

export interface FilterItem {
  id: number
  name: string
  iconUrl?: string
}
