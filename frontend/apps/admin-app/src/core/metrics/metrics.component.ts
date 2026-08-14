import { ChangeDetectionStrategy, Component, computed, inject, resource, signal } from '@angular/core'
import { EventMetricsDaily, EventMetricsWeekly, Participant } from '@open-event/core'
import { MetricsService } from '@open-event/admin'
import { toPromise } from '@open-event/shared'
import { HotToastService } from '@ngxpert/hot-toast'
import { TranslatePipe, TranslateService } from '@ngx-translate/core'
import { DatePipe, DecimalPipe } from '@angular/common'
import { DateTime } from 'luxon'
import { MatButton } from '@angular/material/button'
import { MatIcon } from '@angular/material/icon'
import { MatChipAvatar, MatChipListbox, MatChipOption, MatChipSelectionChange } from '@angular/material/chips'
import { BoardComponent, BoardFilters, BoardToolbarActions } from '../../shared/board/board.component'
import { smoothPath } from './metrics-chart.util'

type Preset = 'today' | 'week' | 'nextweek' | 'month'
type Granularity = 'day' | 'week'
type SortKey = 'total' | 'unique' | 'participants'

interface DateRange {
  start: string
  end: string
}

interface Bucket {
  date: string
  total: number
  unique: number
}

interface EventRow {
  id: number
  title: string
  status: string
  date: string
  total: number
  unique: number
  seats: number
  utilPct: number
  participants: Participant[]
}

const CHART_WIDTH = 1000
const CHART_HEIGHT = 340
const PAD_LEFT = 46
const PAD_RIGHT = 22
const PAD_TOP = 22
const PAD_BOTTOM = 34

@Component({
  selector: 'admin-metrics',
  imports: [TranslatePipe, DatePipe, DecimalPipe, MatButton, MatIcon, MatChipListbox, MatChipOption, MatChipAvatar, BoardComponent, BoardToolbarActions, BoardFilters],
  templateUrl: './metrics.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './metrics.component.scss'
})
export class MetricsComponent {
  private service = inject(MetricsService)
  private toast = inject(HotToastService)
  private translateService = inject(TranslateService)

  readonly preset = signal<Preset>('month')
  readonly granularity = signal<Granularity>('day')
  readonly showUnique = signal(true)
  readonly hoverIndex = signal<number | null>(null)
  readonly sortKey = signal<SortKey>('total')
  readonly sortDir = signal<'asc' | 'desc'>('desc')
  readonly expanded = signal<Set<number>>(new Set())
  readonly recalculating = signal(false)
  readonly monthReference = new Date()

  private range = computed<DateRange>(() => this.computeRange(this.preset()))

  private dailyResource = resource({
    params: this.range,
    loader: (param) => toPromise(this.service.getDaily(param.params.start, param.params.end), param.abortSignal)
  })

  private weeklyResource = resource({
    params: this.range,
    loader: (param) => toPromise(this.service.getWeekly(param.params.start, param.params.end), param.abortSignal)
  })

  readonly loading = computed(() => this.dailyResource.isLoading() || this.weeklyResource.isLoading())

  readonly dateLabel = computed(() => {
    const { start, end } = this.range()
    if (start === end) return this.formatDate(start)
    return `${this.formatDate(start)} – ${this.formatDate(end)}`
  })

  private dailyBuckets = computed<Bucket[]>(() => this.aggregateBuckets(this.dailyResource.value() ?? []))
  private weeklyBuckets = computed<Bucket[]>(() => this.aggregateBuckets(this.weeklyResource.value() ?? []))
  private buckets = computed(() => (this.granularity() === 'day' ? this.dailyBuckets() : this.weeklyBuckets()))

  private chartGeometry = computed(() => {
    const buckets = this.buckets()
    const n = buckets.length
    const maxV = Math.max(4, ...buckets.map((b) => b.total))
    const xF = (i: number) => (n <= 1 ? PAD_LEFT + (CHART_WIDTH - PAD_LEFT - PAD_RIGHT) / 2 : PAD_LEFT + (i * (CHART_WIDTH - PAD_LEFT - PAD_RIGHT)) / (n - 1))
    const yF = (v: number) => CHART_HEIGHT - PAD_BOTTOM - (v / maxV) * (CHART_HEIGHT - PAD_TOP - PAD_BOTTOM)
    const totalPoints = buckets.map((b, i) => ({ x: xF(i), y: yF(b.total) }))
    const uniquePoints = buckets.map((b, i) => ({ x: xF(i), y: yF(b.unique) }))
    const totalPath = smoothPath(totalPoints)
    const uniquePath = smoothPath(uniquePoints)
    const areaPath = totalPoints.length ? `${totalPath} L ${xF(n - 1).toFixed(1)},${CHART_HEIGHT - PAD_BOTTOM} L ${xF(0).toFixed(1)},${CHART_HEIGHT - PAD_BOTTOM} Z` : ''
    const tickVals = [...new Set([0, Math.round(maxV / 2), maxV])]
    const yTicks = tickVals.map((v) => ({ y: yF(v), ty: yF(v) + 4, label: String(v) }))
    const step = Math.max(1, Math.ceil(n / 8))
    const xTicks = buckets.map((b, i) => ({ i, x: xF(i), label: this.bucketLabel(b.date) })).filter((t) => this.granularity() === 'week' || t.i % step === 0 || t.i === n - 1)
    return { buckets, n, xF, yF, totalPath, uniquePath, areaPath, yTicks, xTicks }
  })

  readonly yTicks = computed(() => this.chartGeometry().yTicks)
  readonly xTicks = computed(() => this.chartGeometry().xTicks)
  readonly totalPath = computed(() => this.chartGeometry().totalPath)
  readonly uniquePath = computed(() => this.chartGeometry().uniquePath)
  readonly areaPath = computed(() => this.chartGeometry().areaPath)
  readonly chartEmpty = computed(() => this.kpis().totalViews === 0)

  readonly hoverActive = computed(() => {
    const i = this.hoverIndex()
    return i != null && i >= 0 && i < this.chartGeometry().n
  })

  readonly hover = computed(() => {
    const geo = this.chartGeometry()
    const i = this.hoverIndex()
    if (i == null || i < 0 || i >= geo.n) return undefined
    const b = geo.buckets[i]
    const x = geo.xF(i)
    const top = Math.min(geo.yF(b.total), geo.yF(b.unique))
    return {
      x,
      totalY: geo.yF(b.total),
      uniqueY: geo.yF(b.unique),
      date: this.bucketFullLabel(b.date),
      total: b.total,
      unique: b.unique,
      leftPct: (x / CHART_WIDTH) * 100,
      topPct: (top / CHART_HEIGHT) * 100
    }
  })

  readonly kpis = computed(() => {
    const data = this.dailyResource.value() ?? []
    const totalViews = data.reduce((s, e) => s + e.metrics.reduce((s2, m) => s2 + m.totalCount, 0), 0)
    const uniqueViews = data.reduce((s, e) => s + e.metrics.reduce((s2, m) => s2 + m.uniqueCount, 0), 0)
    const eventCount = data.length
    const totalParticipants = data.reduce((s, e) => s + (e.event.registration?.participants.length ?? 0), 0)
    const totalSeats = data.reduce((s, e) => s + (e.event.registration?.registration.maxGuestAmount ?? 0), 0)
    const utilization = totalSeats > 0 ? Math.round((totalParticipants / totalSeats) * 100) : 0
    const avgViews = eventCount > 0 ? totalViews / eventCount : 0
    return { totalViews, uniqueViews, totalParticipants, totalSeats, utilization, avgViews, eventCount }
  })

  private eventRows = computed<EventRow[]>(() =>
    (this.dailyResource.value() ?? []).map((e) => {
      const seats = e.event.registration?.registration.maxGuestAmount ?? 0
      const participants = e.event.registration?.participants ?? []
      return {
        id: e.event.event.id,
        title: e.event.event.title,
        status: e.event.event.status,
        date: e.event.event.start,
        total: e.metrics.reduce((s, m) => s + m.totalCount, 0),
        unique: e.metrics.reduce((s, m) => s + m.uniqueCount, 0),
        seats,
        utilPct: seats > 0 ? Math.round((participants.length / seats) * 100) : 0,
        participants
      }
    })
  )

  readonly sortedRows = computed(() => {
    const rows = [...this.eventRows()]
    const key = this.sortKey()
    const dir = this.sortDir() === 'asc' ? 1 : -1
    const value = (r: EventRow) => (key === 'participants' ? r.participants.length : key === 'total' ? r.total : r.unique)
    return rows.sort((a, b) => (value(a) - value(b)) * dir)
  })

  readonly eventCountLabel = computed(() => this.eventRows().length)

  private aggregateBuckets(data: (EventMetricsDaily | EventMetricsWeekly)[]): Bucket[] {
    const map = new Map<string, Bucket>()
    for (const entry of data) {
      for (const m of entry.metrics) {
        const bucket = map.get(m.timestamp) ?? { date: m.timestamp, total: 0, unique: 0 }
        bucket.total += m.totalCount
        bucket.unique += m.uniqueCount
        map.set(m.timestamp, bucket)
      }
    }
    return [...map.values()].sort((a, b) => a.date.localeCompare(b.date))
  }

  private computeRange(preset: Preset): DateRange {
    const now = DateTime.now()
    switch (preset) {
      case 'today': {
        const d = now.toISODate() ?? ''
        return { start: d, end: d }
      }
      case 'week':
        return { start: now.startOf('week').toISODate() ?? '', end: now.endOf('week').toISODate() ?? '' }
      case 'nextweek': {
        const next = now.plus({ weeks: 1 })
        return { start: next.startOf('week').toISODate() ?? '', end: next.endOf('week').toISODate() ?? '' }
      }
      case 'month':
      default:
        return { start: now.startOf('month').toISODate() ?? '', end: now.endOf('month').toISODate() ?? '' }
    }
  }

  private formatDate(iso: string): string {
    const d = DateTime.fromISO(iso)
    return d.isValid ? d.toFormat('dd.MM.yyyy') : ''
  }

  private bucketLabel(iso: string): string {
    const d = DateTime.fromISO(iso)
    if (!d.isValid) return ''
    if (this.granularity() === 'week') return `${d.toFormat('dd.MM.')} – ${d.plus({ days: 6 }).toFormat('dd.MM.')}`
    return d.toFormat('dd.MM.')
  }

  private bucketFullLabel(iso: string): string {
    const d = DateTime.fromISO(iso)
    if (!d.isValid) return ''
    if (this.granularity() === 'week') return `${d.toFormat('dd.MM.')} – ${d.plus({ days: 6 }).toFormat('dd.MM.yyyy')}`
    return d.toFormat('dd. MMMM yyyy')
  }

  onPresetChange(event: MatChipSelectionChange, preset: Preset) {
    if (event.selected) this.setPreset(preset)
  }

  setPreset(preset: Preset) {
    this.preset.set(preset)
    this.hoverIndex.set(null)
  }

  setGranularity(granularity: Granularity) {
    this.granularity.set(granularity)
    this.hoverIndex.set(null)
  }

  toggleUnique() {
    this.showUnique.set(!this.showUnique())
  }

  reset() {
    this.preset.set('month')
    this.granularity.set('day')
    this.showUnique.set(true)
    this.hoverIndex.set(null)
    this.sortKey.set('total')
    this.sortDir.set('desc')
    this.expanded.set(new Set())
  }

  onChartMove(event: MouseEvent) {
    const geo = this.chartGeometry()
    if (geo.n === 0) return
    const svg = event.currentTarget as SVGSVGElement
    const rect = svg.getBoundingClientRect()
    const xv = ((event.clientX - rect.left) / rect.width) * CHART_WIDTH
    let i = geo.n <= 1 ? 0 : Math.round((xv - PAD_LEFT) / ((CHART_WIDTH - PAD_LEFT - PAD_RIGHT) / (geo.n - 1)))
    i = Math.max(0, Math.min(geo.n - 1, i))
    if (i !== this.hoverIndex()) this.hoverIndex.set(i)
  }

  onChartLeave() {
    this.hoverIndex.set(null)
  }

  setSort(key: SortKey) {
    if (this.sortKey() === key) this.sortDir.set(this.sortDir() === 'asc' ? 'desc' : 'asc')
    else {
      this.sortKey.set(key)
      this.sortDir.set('desc')
    }
  }

  sortArrow(key: SortKey): string {
    if (this.sortKey() !== key) return ''
    return this.sortDir() === 'asc' ? '▲' : '▼'
  }

  isExpanded(id: number): boolean {
    return this.expanded().has(id)
  }

  toggleExpand(id: number) {
    if (!this.eventRows().find((r) => r.id === id)?.participants.length) return
    const next = new Set(this.expanded())
    if (next.has(id)) next.delete(id)
    else next.add(id)
    this.expanded.set(next)
  }

  initials(name: string): string {
    return name
      .split(' ')
      .map((w) => w[0])
      .filter(Boolean)
      .slice(0, 2)
      .join('')
      .toUpperCase()
  }

  reload() {
    this.dailyResource.reload()
    this.weeklyResource.reload()
  }

  recalculateWeekly() {
    if (this.recalculating()) return
    this.recalculating.set(true)
    this.service.recalculateWeekly().subscribe({
      next: () => {
        this.translateService.get('metrics.message.recalculateWeeklySuccess').subscribe((t) => this.toast.success(t))
        this.recalculating.set(false)
        this.weeklyResource.reload()
      },
      error: () => {
        this.translateService.get('metrics.message.recalculateWeeklyError').subscribe((t) => this.toast.error(t))
        this.recalculating.set(false)
      }
    })
  }
}
