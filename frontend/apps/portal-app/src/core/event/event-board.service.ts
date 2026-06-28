import { computed, effect, inject, Injectable, resource, signal } from '@angular/core'
import { PageEvent } from '@angular/material/paginator'
import { DateTime } from 'luxon'
import { EventSearchEntry, EventSearchRequest } from '@open-event/core'
import { EventService } from '@open-event/portal'
import { EventBoardDateRange } from '@open-event/ui'
import { toPromise } from '@open-event/shared'

@Injectable({ providedIn: 'root' })
export class EventBoardService {
  private service = inject(EventService)
  private query = signal('')
  private fromDate = signal<string | undefined>(undefined)
  private toDate = signal<string | undefined>(undefined)
  private availableOnly = signal(false)
  readonly showAvailableOnly = computed(() => this.availableOnly())
  private includeHistory = signal(false)
  readonly showHistory = computed(() => this.includeHistory())
  private page = signal(0)
  private size = signal(200)
  private infiniteScrollMode = signal(false)
  private layoutSignal = signal<'cards' | 'rows' | 'calendar' | 'map'>('cards')
  readonly layout = this.layoutSignal.asReadonly()
  private categoryFilterSignal = signal<Set<string>>(new Set())
  readonly categoryFilter = this.categoryFilterSignal.asReadonly()
  private audienceFilterSignal = signal<Set<string>>(new Set())
  readonly audienceFilter = this.audienceFilterSignal.asReadonly()

  private criteria = computed(() => ({
    request: new EventSearchRequest(
      this.query(), this.fromDate(), this.toDate(),
      false, false,
      this.availableOnly(),
      Array.from(this.categoryFilterSignal()),
      false, false,
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
    this.fromDate.set(DateTime.now().startOf('day').toISODate() ?? undefined)
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

  setDateRange(range: EventBoardDateRange) {
    this.fromDate.set(range.start)
    this.toDate.set(range.end)
    this.page.set(0)
  }

  toggleAvailableEvents() {
    this.availableOnly.update((v) => !v)
    this.page.set(0)
  }

  toggleShowHistory() {
    this.includeHistory.update((v) => !v)
    this.fromDate.set(this.includeHistory() ? undefined : (DateTime.now().startOf('day').toISODate() ?? undefined))
    this.toDate.set(undefined)
    this.page.set(0)
  }

  setLayout(layout: 'cards' | 'rows' | 'calendar' | 'map') {
    this.layoutSignal.set(layout)
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
    this.availableOnly.set(false)
    this.includeHistory.set(false)
    this.categoryFilterSignal.set(new Set())
    this.audienceFilterSignal.set(new Set())
    this.fromDate.set(DateTime.now().startOf('day').toISODate() ?? undefined)
    this.toDate.set(undefined)
    this.page.set(0)
  }

  reload() {
    this.page.set(0)
    this.searchResource.reload()
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
}
