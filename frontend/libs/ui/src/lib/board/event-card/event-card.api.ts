export interface EventCardEntry {
  link: string | string[]
  title: string
  ownerName: string
  start: string
  finish: string
  hasLocation: boolean
  street?: string
  streetNumber?: string
  zip?: string
  city?: string
  country?: string
  categories: string[]
  tags: string[]
}
