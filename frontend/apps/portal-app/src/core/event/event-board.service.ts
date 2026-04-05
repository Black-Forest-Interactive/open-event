import { computed, effect, inject, Injectable, resource, signal } from '@angular/core'
import { PageEvent } from '@angular/material/paginator'
import { FormControl, FormGroup } from '@angular/forms'
import { DateTime } from 'luxon'
import { EventSearchEntry, EventSearchRequest } from '@open-event/core'
import { EventService } from '@open-event/portal'
import { toPromise } from '@open-event/shared'

@Injectable({ providedIn: 'root' })
export class EventBoardService {
  private service = inject(EventService)

  private query = signal('')
  private fromDate = signal<string | undefined>(undefined)
  private toDate = signal<string | undefined>(undefined)
  private ownOnly = signal(false)
  private availableOnly = signal(false)
  private participatingOnly = signal(false)
  private includeHistory = signal(false)
  private page = signal(0)
  private size = signal(200)

  readonly showOwnOnly = computed(() => this.ownOnly())
  readonly showAvailableOnly = computed(() => this.availableOnly())
  readonly showParticipatingOnly = computed(() => this.participatingOnly())
  readonly showHistory = computed(() => this.includeHistory())

  infiniteScrollMode = false
  filterToolbarVisible = true
  preselection: string | undefined

  range = new FormGroup({
    start: new FormControl<DateTime | null>(null),
    end: new FormControl<DateTime | null>(null)
  })

  private criteria = computed(() => ({
    request: new EventSearchRequest(this.query(), this.fromDate(), this.toDate(), this.ownOnly(), this.participatingOnly(), this.availableOnly()),
    page: this.page(),
    size: this.size()
  }))

  private searchResource = resource({
    params: this.criteria,
    loader: (p) => toPromise(this.service.search(p.params.request, p.params.page, p.params.size), p.abortSignal)
  })

  private loaded = signal<EventSearchEntry[]>([])
  readonly entries = computed(() => this.loaded())
  readonly reloading = this.searchResource.isLoading
  readonly totalSize = computed(() => this.searchResource.value()?.result.totalSize ?? 0)
  readonly pageIndex = computed(() => this.searchResource.value()?.result.pageable.number ?? 0)
  readonly pageSize = computed(() => this.searchResource.value()?.result.pageable.size ?? 200)
  readonly hasMoreElements = computed(() => {
    const result = this.searchResource.value()?.result
    if (!result) return false
    return result.content.length !== 0 && result.pageable.number !== result.totalPages - 1
  })

  constructor() {
    this.updateRange(null, null)
    effect(() => {
      const result = this.searchResource.value()
      if (!result) return
      const page = result.result.pageable.number
      if (this.infiniteScrollMode && page > 0) {
        this.loaded.update(prev => [...prev, ...result.result.content])
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
    this.ownOnly.update(v => !v)
    this.page.set(0)
  }

  toggleAvailableEvents() {
    this.availableOnly.update(v => !v)
    this.page.set(0)
  }

  toggleParticipatingEvents() {
    this.participatingOnly.update(v => !v)
    this.page.set(0)
  }

  toggleShowHistory() {
    this.includeHistory.update(v => !v)
    this.handleRangeChanged()
  }

  handleDatePickerChanged() {
    this.preselection = undefined
    this.handleRangeChanged()
  }

  handlePreselectionChanged(selected: boolean, value: string) {
    if (this.preselection === value) return
    this.preselection = value

    if (!selected) {
      this.updateRange(null, null)
    } else if (value === 'today') {
      this.updateRange(DateTime.now(), DateTime.now())
    } else if (value === 'this_week') {
      const now = DateTime.now()
      this.updateRange(now.startOf('week'), now.endOf('week'))
    } else if (value === 'next_week') {
      const next = DateTime.now().plus({ weeks: 1 })
      this.updateRange(next.startOf('week'), next.endOf('week'))
    }
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
    this.toDate.set(end ? end.endOf('day').toISODate() ?? undefined : undefined)
    this.page.set(0)
  }

  resetFilter() {
    this.query.set('')
    this.ownOnly.set(false)
    this.availableOnly.set(false)
    this.participatingOnly.set(false)
    this.includeHistory.set(false)
    this.preselection = undefined
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
}
