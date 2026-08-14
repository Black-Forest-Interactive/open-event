import { Component, computed, DestroyRef, effect, inject, resource, signal, TemplateRef, viewChild, ChangeDetectionStrategy } from '@angular/core'
import { BreakpointObserver } from '@angular/cdk/layout'
import { toSignal } from '@angular/core/rxjs-interop'
import { map } from 'rxjs'
import { DateTime } from 'luxon'
import { EventSearchEntry, EventSearchRequest } from '@open-event/core'
import { EventService } from '@open-event/portal'
import { BoardSearchComponent, EventBoardDateRange } from '@open-event/ui'
import { LoadingBarComponent, toPromise, TourService } from '@open-event/shared'
import { MatBottomSheet } from '@angular/material/bottom-sheet'
import { MatIcon } from '@angular/material/icon'
import { MatButton, MatIconButton } from '@angular/material/button'
import { MatBadge } from '@angular/material/badge'
import { TranslatePipe } from '@ngx-translate/core'
import { RouterLink } from '@angular/router'
import { EventBoardListComponent } from '../event-board-list/event-board-list.component'
import { EventBoardNavbarComponent } from '../event-board-navbar/event-board-navbar.component'
import { EventBoardFilterComponent } from '../event-board-filter/event-board-filter.component'
import { eventBoardOwnTour } from './event-board-own.tour'

@Component({
  selector: 'portal-event-board-own',
  templateUrl: './event-board-own.component.html',
  imports: [
    EventBoardListComponent,
    EventBoardNavbarComponent,
    EventBoardFilterComponent,
    BoardSearchComponent,
    LoadingBarComponent,
    MatIcon,
    MatButton,
    MatIconButton,
    MatBadge,
    RouterLink,
    TranslatePipe
  ],
  changeDetection: ChangeDetectionStrategy.Eager
})
export class EventBoardOwnComponent {
  private eventService = inject(EventService)
  private responsive = inject(BreakpointObserver)
  private bottomSheet = inject(MatBottomSheet)
  private tourService = inject(TourService)
  private destroyRef = inject(DestroyRef)
  private filterSheet = viewChild<TemplateRef<unknown>>('filterSheet')

  readonly mobileView = toSignal(this.responsive.observe(['(min-width: 768px)']).pipe(map((s) => !s.matches)), { initialValue: false })

  private query = signal('')
  private fromDate = signal<string | undefined>(undefined)
  private toDate = signal<string | undefined>(undefined)
  private page = signal(0)
  private size = signal(200)
  private infiniteScrollMode = signal(false)
  private includeHistory = signal(true)
  readonly showHistory = this.includeHistory.asReadonly()
  private categoryFilterSignal = signal<Set<string>>(new Set())
  readonly categoryFilter = this.categoryFilterSignal.asReadonly()
  private audienceFilterSignal = signal<Set<string>>(new Set())
  readonly audienceFilter = this.audienceFilterSignal.asReadonly()
  readonly activeFilterCount = computed(() => this.categoryFilterSignal().size + this.audienceFilterSignal().size)

  private criteria = computed(() => ({
    request: new EventSearchRequest(
      this.query(), this.fromDate(), this.toDate(),
      true, false, false,
      Array.from(this.categoryFilterSignal()),
      false, false,
      Array.from(this.audienceFilterSignal())
    ),
    page: this.page(),
    size: this.size()
  }))

  private searchResource = resource({
    params: this.criteria,
    loader: (p) => toPromise(this.eventService.search(p.params.request, p.params.page, p.params.size), p.abortSignal)
  })
  readonly reloading = this.searchResource.isLoading
  readonly hasMoreElements = computed(() => {
    const result = this.searchResource.value()?.result
    if (!result) return false
    return result.content.length !== 0 && result.pageable.number !== result.totalPages - 1
  })
  private pageIndex = computed(() => this.searchResource.value()?.result.pageable.number ?? 0)
  private loaded = signal<EventSearchEntry[]>([])
  readonly entries = computed(() => this.loaded())

  constructor() {
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
    effect(() => {
      this.infiniteScrollMode.set(this.mobileView())
    })
    this.tourService.register(eventBoardOwnTour, () => !this.reloading())
    this.destroyRef.onDestroy(() => this.tourService.unregister(eventBoardOwnTour.id))
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

  toggleHistory() {
    this.includeHistory.update((v) => !v)
    this.fromDate.set(this.includeHistory() ? undefined : (DateTime.now().startOf('day').toISODate() ?? undefined))
    this.toDate.set(undefined)
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
    this.categoryFilterSignal.set(new Set())
    this.audienceFilterSignal.set(new Set())
    this.includeHistory.set(true)
    this.fromDate.set(undefined)
    this.toDate.set(undefined)
    this.page.set(0)
  }

  openFilter() {
    const sheet = this.filterSheet()
    if (sheet) this.bottomSheet.open(sheet)
  }

  onScroll() {
    if (this.reloading() || !this.hasMoreElements()) return
    this.page.set(this.pageIndex() + 1)
  }

  reload() {
    this.page.set(0)
    this.searchResource.reload()
  }
}
