import { Component, computed, effect, inject, resource, signal } from '@angular/core'
import { EventService, PublicEvent, PublicEventSearchRequest } from '@open-event/external'
import { FormControl, FormGroup } from '@angular/forms'
import { DateTime } from 'luxon'
import { toPromise } from '@open-event/shared'
import { PageEvent } from '@angular/material/paginator'
import { BreakpointObserver } from '@angular/cdk/layout'
import { toSignal } from '@angular/core/rxjs-interop'
import { map } from 'rxjs'

@Component({
  selector: 'app-event-board',
  imports: [],
  templateUrl: './event-board.component.html',
  styleUrl: './event-board.component.scss'
})
export class EventBoardComponent {
  private service = inject(EventService)
  private responsive = inject(BreakpointObserver)

  readonly mobileView = toSignal(this.responsive.observe(['(min-width: 768px)']).pipe(map((s) => !s.matches)), { initialValue: false })
  readonly mode = signal('list')

  private query = signal('')
  private fromDate = signal<string | undefined>(undefined)
  private toDate = signal<string | undefined>(undefined)
  private availableOnly = signal(false)
  private includeHistory = signal(false)
  private page = signal(0)
  private size = signal(200)
  readonly showAvailableOnly = computed(() => this.availableOnly())
  readonly showHistory = computed(() => this.includeHistory())

  private infiniteScrollMode = signal(false)
  private filterToolbarVisible = signal(true)
  private preselectionSignal = signal<string | undefined>(undefined)
  readonly preselection = this.preselectionSignal.asReadonly()

  range = new FormGroup({
    start: new FormControl<DateTime | null>(null),
    end: new FormControl<DateTime | null>(null)
  })

  private criteria = computed(() => ({
    request: new PublicEventSearchRequest(this.query(), this.fromDate(), this.toDate(), this.availableOnly()),
    page: this.page(),
    size: this.size()
  }))

  private searchResource = resource({
    params: this.criteria,
    loader: (p) => toPromise(this.service.search(p.params.request, p.params.page, p.params.size), p.abortSignal)
  })

  readonly entries = signal<PublicEvent[]>([])
  readonly reloading = this.searchResource.isLoading
  readonly totalSize = computed(() => this.searchResource.value()?.totalSize ?? 0)
  readonly pageIndex = computed(() => this.searchResource.value()?.pageable.number ?? 0)
  readonly pageSize = computed(() => this.searchResource.value()?.pageable.size ?? 200)
  readonly hasMoreElements = computed(() => {
    const result = this.searchResource.value()
    if (!result) return false
    return result.content.length !== 0 && result.pageable.number !== result.totalPages - 1
  })

  constructor() {
    this.updateRange(null, null)
    effect(() => {
      const result = this.searchResource.value()
      if (!result) return
      const page = result.pageable.number
      if (this.infiniteScrollMode() && page > 0) {
        this.entries.update((prev) => [...prev, ...result.content])
      } else {
        this.entries.set(result.content)
      }
    })

    effect(() => {
      const mobile = this.mobileView()
      this.setFilterToolbarVisible(!mobile)
      this.setInfiniteScrollMode(mobile)
    })
  }

  setQuery(val: string) {
    if (this.query() === val) return
    this.query.set(val)
    this.page.set(0)
  }

  toggleAvailableEvents() {
    this.availableOnly.update((v) => !v)
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
    this.toDate.set(end ? (end.endOf('day').toISODate() ?? undefined) : undefined)
    this.page.set(0)
  }

  resetFilter() {
    this.query.set('')
    this.availableOnly.set(false)
    this.includeHistory.set(false)
    this.preselectionSignal.set(undefined)
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
}
