import { Component, computed, effect, inject, resource, signal, TemplateRef, viewChild } from '@angular/core'
import { NgTemplateOutlet } from '@angular/common'
import { BreakpointObserver } from '@angular/cdk/layout'
import { toSignal } from '@angular/core/rxjs-interop'
import { map } from 'rxjs'
import { DateTime } from 'luxon'
import { EventSearchEntry, EventSearchRequest } from '@open-event/core'
import { EventService } from '@open-event/portal'
import { EventBoardDateFilterComponent, EventBoardDateRange, BoardSearchComponent } from '@open-event/ui'
import { LoadingBarComponent, toPromise } from '@open-event/shared'
import { MatBottomSheet } from '@angular/material/bottom-sheet'
import { MatIcon } from '@angular/material/icon'
import { MatButton, MatIconButton } from '@angular/material/button'
import { MatButtonToggle, MatButtonToggleGroup } from '@angular/material/button-toggle'
import { TranslatePipe } from '@ngx-translate/core'
import { EventBoardListComponent } from '../event-board-list/event-board-list.component'
import { EventBoardNavbarComponent } from '../event-board-navbar/event-board-navbar.component'
import { CategoryFilterComponent } from '../event-board-filter/category-filter/category-filter.component'
import { RouterLink } from '@angular/router'

@Component({
  selector: 'portal-event-board-own',
  templateUrl: './event-board-own.component.html',
  imports: [
    NgTemplateOutlet,
    EventBoardListComponent,
    EventBoardNavbarComponent,
    EventBoardDateFilterComponent,
    CategoryFilterComponent,
    BoardSearchComponent,
    LoadingBarComponent,
    MatIcon,
    MatButton,
    MatIconButton,
    MatButtonToggleGroup,
    MatButtonToggle,
    RouterLink,
    TranslatePipe
  ],
  standalone: true
})
export class EventBoardOwnComponent {
  private eventService = inject(EventService)
  private responsive = inject(BreakpointObserver)
  private bottomSheet = inject(MatBottomSheet)
  private filterSheet = viewChild<TemplateRef<unknown>>('filterSheet')

  readonly mobileView = toSignal(this.responsive.observe(['(min-width: 768px)']).pipe(map((s) => !s.matches)), { initialValue: false })

  private query = signal('')
  private fromDate = signal<string | undefined>(undefined)
  private toDate = signal<string | undefined>(undefined)
  private page = signal(0)
  private size = signal(200)
  private infiniteScrollMode = signal(false)
  readonly includeHistory = signal(false)
  readonly categoryFilter = signal<Set<string>>(new Set())
  readonly layout = signal<'cards' | 'rows'>('rows')

  private criteria = computed(() => ({
    request: new EventSearchRequest(
      this.query(), this.fromDate(), this.toDate(),
      true, false, false,
      Array.from(this.categoryFilter()),
      false, false, []
    ),
    page: this.page(),
    size: this.size()
  }))

  private searchResource = resource({
    params: this.criteria,
    loader: (p) => toPromise(this.eventService.search(p.params.request, p.params.page, p.params.size), p.abortSignal)
  })
  readonly reloading = this.searchResource.isLoading
  readonly totalSize = computed(() => this.searchResource.value()?.result.totalSize ?? 0)
  private pageIndex = computed(() => this.searchResource.value()?.result.pageable.number ?? 0)
  readonly hasMoreElements = computed(() => {
    const result = this.searchResource.value()?.result
    if (!result) return false
    return result.content.length !== 0 && result.pageable.number !== result.totalPages - 1
  })
  private loaded = signal<EventSearchEntry[]>([])
  readonly entries = computed(() => this.loaded())

  constructor() {
    this.applyDefaultRange()
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
  }

  setQuery(val: string) {
    if (this.query() === val) return
    this.query.set(val)
    this.page.set(0)
  }

  handleRangeChanged(range: EventBoardDateRange) {
    this.fromDate.set(range.start)
    this.toDate.set(range.end)
    this.page.set(0)
  }

  handleReset() {
    this.query.set('')
    this.categoryFilter.set(new Set())
    this.includeHistory.set(false)
    this.applyDefaultRange()
    this.page.set(0)
  }

  toggleHistory() {
    this.includeHistory.update((v) => !v)
    this.applyDefaultRange()
    this.page.set(0)
  }

  toggleCategory(name: string) {
    this.categoryFilter.update((prev) => {
      const next = new Set(prev)
      if (next.has(name)) next.delete(name)
      else next.add(name)
      return next
    })
    this.page.set(0)
  }

  onScroll() {
    if (this.reloading() || !this.hasMoreElements()) return
    this.page.set(this.pageIndex() + 1)
  }

  openFilter() {
    const sheet = this.filterSheet()
    if (sheet) this.bottomSheet.open(sheet)
  }

  private applyDefaultRange() {
    this.fromDate.set(this.includeHistory() ? undefined : (DateTime.now().startOf('day').toISODate() ?? undefined))
    this.toDate.set(undefined)
  }
}
