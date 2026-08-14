import { EventInfo } from '../event/event.api'

export interface DailyBreakdownEntry {
  day: string
  totalCount: number
  uniqueCount: number
}

export interface DailyMetrics {
  id: string
  resource: number
  action: string
  timestamp: string
  totalCount: number
  uniqueCount: number
}

export interface WeeklyMetrics {
  id: string
  resource: number
  action: string
  timestamp: string
  totalCount: number
  uniqueCount: number
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
