import { EventInfo } from '../event/event.api'

export type MetricsSource = 'PORTAL' | 'EXTERNAL'

export interface MetricsEntry {
  source: MetricsSource
  totalCount: number
  uniqueCount: number
}

export interface DailyBreakdownEntry {
  day: string
  entries: MetricsEntry[]
}

export interface DailyMetrics {
  id: string
  resource: number
  action: string
  timestamp: string
  totalCount: number
  uniqueCount: number
  entries: MetricsEntry[]
}

export interface WeeklyMetrics {
  id: string
  resource: number
  action: string
  timestamp: string
  totalCount: number
  uniqueCount: number
  entries: MetricsEntry[]
  dailyBreakdown: DailyBreakdownEntry[]
}

export interface EventMetricsDaily {
  event: EventInfo
  metrics: DailyMetrics[]
}

export interface EventMetricsWeekly {
  event: EventInfo
  metrics: WeeklyMetrics[]
}
