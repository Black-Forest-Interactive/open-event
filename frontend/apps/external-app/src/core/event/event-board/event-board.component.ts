import { Component, computed, effect, inject, resource, signal } from '@angular/core'
import { EventService, PublicEvent, PublicEventSearchRequest } from '@open-event/external'
import { DateTime } from 'luxon'
import { LoadingBarComponent, ScrollNearEndDirective, toPromise } from '@open-event/shared'
import { BreakpointObserver } from '@angular/cdk/layout'
import { toSignal } from '@angular/core/rxjs-interop'
import { map } from 'rxjs'
import { ActivatedRoute } from '@angular/router'
import { BoardSearchComponent, EventBoardDateFilterComponent, EventBoardDateRange } from '@open-event/ui'
import { MatButton } from '@angular/material/button'
import { MatButtonToggle, MatButtonToggleGroup } from '@angular/material/button-toggle'
import { MatIcon } from '@angular/material/icon'
import { TranslatePipe } from '@ngx-translate/core'
import { EventCardComponent } from '../event-card/event-card.component'
import { EventRowComponent } from '../event-row/event-row.component'
import { EventBoardCalendarComponent } from '../event-board-calendar/event-board-calendar.component'

type BoardLayout = 'cards' | 'rows' | 'calendar'

@Component({
  selector: 'app-event-board',
  templateUrl: './event-board.component.html',
  imports: [
    BoardSearchComponent,
    EventBoardDateFilterComponent,
    EventCardComponent,
    EventRowComponent,
    EventBoardCalendarComponent,
    LoadingBarComponent,
    ScrollNearEndDirective,
    MatButton,
    MatButtonToggle,
    MatButtonToggleGroup,
    MatIcon,
    TranslatePipe
  ]
})
export class EventBoardComponent {
  readonly layout = signal<BoardLayout>('cards')
  readonly filterVisible = signal(false)
  readonly showAvailableOnly = signal(false)
  readonly showHistory = signal(false)
  readonly entries = signal<PublicEvent[]>([])
  private service = inject(EventService)
  private responsive = inject(BreakpointObserver)
  readonly mobileView = toSignal(this.responsive.observe(['(min-width: 768px)']).pipe(map((s) => !s.matches)), { initialValue: false })
  private route = inject(ActivatedRoute)
  private routeKey = toSignal(this.route.paramMap.pipe(map((p) => p.get('id') ?? '')), { initialValue: '' })
  private query = signal('')
  private fromDate = signal<string | undefined>(undefined)
  private toDate = signal<string | undefined>(undefined)
  private page = signal(0)
  private size = signal(200)
  private infiniteScrollMode = signal(false)
  private criteria = computed(() => ({
    key: this.routeKey(),
    request: new PublicEventSearchRequest(this.query(), this.fromDate(), this.toDate(), this.showAvailableOnly(), false),
    page: this.page(),
    size: this.size()
  }))
  private searchResource = resource({
    params: this.criteria,
    loader: (p) => toPromise(this.service.search(p.params.key, p.params.request, p.params.page, p.params.size), p.abortSignal)
  })
  readonly reloading = this.searchResource.isLoading
  readonly totalSize = computed(() => this.searchResource.value()?.totalSize ?? 0)
  readonly hasMoreElements = computed(() => {
    const result = this.searchResource.value()
    if (!result) return false
    return result.content.length !== 0 && result.pageable.number !== result.totalPages - 1
  })

  private pageIndex = computed(() => this.searchResource.value()?.pageable.number ?? 0)

  constructor() {
    this.applyDefaultRange()
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
      this.infiniteScrollMode.set(this.mobileView())
    })
  }

  setLayout(layout: BoardLayout) {
    this.layout.set(layout)
  }

  setQuery(val: string) {
    if (this.query() === val) return
    this.query.set(val)
    this.page.set(0)
  }

  toggleAvailableEvents() {
    this.showAvailableOnly.update((v) => !v)
    this.page.set(0)
  }

  toggleShowHistory() {
    this.showHistory.update((v) => !v)
    this.applyDefaultRange()
  }

  handleRangeChanged(range: EventBoardDateRange) {
    this.fromDate.set(range.start)
    this.toDate.set(range.end)
    this.page.set(0)
  }

  handleReset() {
    this.query.set('')
    this.showAvailableOnly.set(false)
    this.showHistory.set(false)
    this.applyDefaultRange()
    this.page.set(0)
  }

  onScroll() {
    if (this.reloading() || !this.hasMoreElements()) return
    this.page.set(this.pageIndex() + 1)
  }

  private applyDefaultRange() {
    const start = this.showHistory() ? undefined : (DateTime.now().startOf('day').toISODate() ?? undefined)
    this.fromDate.set(start)
    this.toDate.set(undefined)
    this.page.set(0)
  }
}
