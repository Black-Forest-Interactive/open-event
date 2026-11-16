import { EventSearchEntry, FinderAnswer, FinderInfo } from '@open-event/core'

export type FinderTab = 'discover' | FinderAnswer

export interface FinderStats {
  open: number
  yes: number
  maybe: number
  no: number
  total: number
  rated: number
}

export const FINDER_ANSWER_ICONS: Record<FinderAnswer, string> = { YES: 'thumb_up', MAYBE: 'schedule', NO: 'thumb_down' }

export function finderInfoStats(info: FinderInfo): FinderStats {
  return toStats(info.countOpen, info.countYes, info.countMaybe, info.countNo)
}

export function finderStats(entries: EventSearchEntry[], ratings: Record<string, FinderAnswer>): FinderStats {
  const count = { YES: 0, MAYBE: 0, NO: 0 }
  let open = 0
  entries.forEach((e) => {
    const answer = ratings[e.id]
    if (answer) count[answer]++
    else open++
  })
  return toStats(open, count.YES, count.MAYBE, count.NO)
}

function toStats(open: number, yes: number, maybe: number, no: number): FinderStats {
  const total = open + yes + maybe + no
  return { open, yes, maybe, no, total, rated: total - open }
}

export function progressPercent(stats: FinderStats): number {
  return stats.total ? Math.round((stats.rated / stats.total) * 100) : 0
}

/** Duration between start and finish in minutes. */
export function durationMinutes(entry: EventSearchEntry): number {
  return Math.max(0, Math.round((new Date(entry.finish).getTime() - new Date(entry.start).getTime()) / 60000))
}

/** Long texts may contain legacy HTML, the card only shows the plain text (clamped). */
export function plainText(value: string | undefined): string {
  if (!value) return ''
  return new DOMParser().parseFromString(value, 'text/html').body.textContent?.trim() ?? ''
}
