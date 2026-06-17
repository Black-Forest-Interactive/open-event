import { computed, effect, inject, Injectable, resource, signal } from '@angular/core'
import { PageEvent } from '@angular/material/paginator'
import { FormControl, FormGroup } from '@angular/forms'
import { DateTime } from 'luxon'
import { EventSearchEntry, EventSearchRequest } from '@open-event/core'
import { EventService } from '@open-event/portal'
import { toPromise } from '@open-event/shared'

@Injectable({ providedIn: 'root' })
export class EventBoardService {
  range = new FormGroup({
    start: new FormControl<DateTime | null>(null),
    end: new FormControl<DateTime | null>(null)
  })
  private service = inject(EventService)
  private query = signal('')
  private fromDate = signal<string | undefined>(undefined)
  private toDate = signal<string | undefined>(undefined)
  private ownOnly = signal(false)
  readonly showOwnOnly = computed(() => this.ownOnly())
  private availableOnly = signal(false)
  readonly showAvailableOnly = computed(() => this.availableOnly())
  private participatingOnly = signal(false)
  readonly showParticipatingOnly = computed(() => this.participatingOnly())
  private includeHistory = signal(false)
  readonly showHistory = computed(() => this.includeHistory())
  private page = signal(0)
  private size = signal(200)
  private infiniteScrollMode = signal(false)
  private filterToolbarVisible = signal(true)
  private preselectionSignal = signal<string | undefined>(undefined)
  readonly preselection = this.preselectionSignal.asReadonly()
  private layoutSignal = signal<'cards' | 'rows' | 'calendar' | 'map'>('cards')
  readonly layout = this.layoutSignal.asReadonly()
  private categoryFilterSignal = signal<Set<string>>(new Set())
  readonly categoryFilter = this.categoryFilterSignal.asReadonly()
  private audienceFilterSignal = signal<Set<string>>(new Set())
  readonly audienceFilter = this.audienceFilterSignal.asReadonly()
  private navViewSignal = signal<'all' | 'saved' | 'regs'>('all')
  readonly navView = this.navViewSignal.asReadonly()
  private criteria = computed(() => ({
    request: new EventSearchRequest(
      this.query(), this.fromDate(), this.toDate(), this.ownOnly(),
      this.navViewSignal() === 'regs' || this.participatingOnly(),
      this.availableOnly(),
      Array.from(this.categoryFilterSignal()),
      this.navViewSignal() === 'saved',
      false,
      Array.from(this.audienceFilterSignal())
    ),
    page: this.page(),
    size: this.size()
  }))

  private searchResource = resource({
    params: this.criteria,
    loader: (p) => toPromise(this.service.search(p.params.request, p.params.page, p.params.size), p.abortSignal)
  })
  readonly reloading = this.searchResource.isLoading
  readonly totalSize = computed(() => this.searchResource.value()?.result.totalSize ?? 0)
  readonly pageIndex = computed(() => this.searchResource.value()?.result.pageable.number ?? 0)
  readonly pageSize = computed(() => this.searchResource.value()?.result.pageable.size ?? 200)
  readonly hasMoreElements = computed(() => {
    const result = this.searchResource.value()?.result
    if (!result) return false
    return result.content.length !== 0 && result.pageable.number !== result.totalPages - 1
  })
  private loaded = signal<EventSearchEntry[]>([])
  readonly entries = computed(() => this.loaded())

  constructor() {
    this.updateRange(null, null)
    effect(() => {
      const result = this.searchResource.value()
      if (!result) return
      const page = result.result.pageable.number
      if (this.infiniteScrollMode() && page > 0) {
        this.loaded.update((prev) => [...prev, ...result.result.content])
      } else {
        this.loaded.set(result.result.content)
      }
    })
  }

  setQuery(val: string) {
    if (this.query() === val) return
    this.query.set(val)
    this.page.set(0)
  }

  toggleOwnEvents() {
    this.ownOnly.update((v) => !v)
    this.page.set(0)
  }

  toggleAvailableEvents() {
    this.availableOnly.update((v) => !v)
    this.page.set(0)
  }

  toggleParticipatingEvents() {
    this.participatingOnly.update((v) => !v)
    this.page.set(0)
  }

  toggleShowHistory() {
    this.includeHistory.update((v) => !v)
    this.handleRangeChanged()
  }

  handleDatePickerChanged() {
    this.preselectionSignal.set(undefined)
    this.handleRangeChanged()
  }

  handlePreselectionChanged(selected: boolean, value: string) {
    if (this.preselectionSignal() === value) return
    this.preselectionSignal.set(value)

    if (!selected || value === 'any') {
      if (value === 'any') this.includeHistory.set(false)
      this.updateRange(null, null)
    } else if (value === 'today') {
      this.updateRange(DateTime.now(), DateTime.now())
    } else if (value === 'weekend') {
      const now = DateTime.now()
      this.updateRange(now.startOf('week').plus({ days: 5 }), now.endOf('week'))
    } else if (value === 'next_week') {
      const next = DateTime.now().plus({ weeks: 1 })
      this.updateRange(next.startOf('week'), next.endOf('week'))
    }
  }

  setLayout(layout: 'cards' | 'rows' | 'calendar' | 'map') {
    this.layoutSignal.set(layout)
  }

  setNavView(view: 'all' | 'saved' | 'regs') {
    this.navViewSignal.set(view)
    this.page.set(0)
  }

  toggleCategory(name: string) {
    this.categoryFilterSignal.update((prev) => {
      const next = new Set(prev)
      if (next.has(name)) next.delete(name)
      else next.add(name)
      return next
    })
    this.page.set(0)
  }

  toggleAudience(name: string) {
    this.audienceFilterSignal.update((prev) => {
      const next = new Set(prev)
      if (next.has(name)) next.delete(name)
      else next.add(name)
      return next
    })
    this.page.set(0)
  }

  resetFilter() {
    this.query.set('')
    this.ownOnly.set(false)
    this.availableOnly.set(false)
    this.participatingOnly.set(false)
    this.includeHistory.set(false)
    this.categoryFilterSignal.set(new Set())
    this.audienceFilterSignal.set(new Set())
    this.preselectionSignal.set(undefined)
    this.navViewSignal.set('all')
    this.updateRange(null, null)
  }

  onScroll() {
    if (this.reloading() || !this.hasMoreElements()) return
    this.page.set(this.pageIndex() + 1)
  }

  handlePageChange(event: PageEvent) {
    this.page.set(event.pageIndex)
    this.size.set(event.pageSize)
  }

  setInfiniteScrollMode(value: boolean) {
    this.infiniteScrollMode.set(value)
  }

  setFilterToolbarVisible(value: boolean) {
    this.filterToolbarVisible.set(value)
  }

  private handleRangeChanged() {
    const { start, end } = this.range.value
    this.updateRange(start, end)
  }

  private updateRange(start: DateTime | null | undefined, end: DateTime | null | undefined) {
    this.range.setValue({ start: start ?? null, end: end ?? null })

    let startDate: DateTime | null = null
    if (start) {
      startDate = start.startOf('day')
    } else if (!this.includeHistory()) {
      startDate = DateTime.now().startOf('day')
    }

    this.fromDate.set(startDate?.toISODate() ?? undefined)
    this.toDate.set(end ? (end.endOf('day').toISODate() ?? undefined) : undefined)
    this.page.set(0)
  }
}
