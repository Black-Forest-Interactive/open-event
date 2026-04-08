import { Component, computed, effect, inject, resource, signal } from '@angular/core'
import { EventService, PublicEvent, PublicEventSearchRequest } from '@open-event/external'
import { DateTime } from 'luxon'
import { toPromise } from '@open-event/shared'
import { BreakpointObserver } from '@angular/cdk/layout'
import { toSignal } from '@angular/core/rxjs-interop'
import { map } from 'rxjs'
import { ActivatedRoute } from '@angular/router'
import { BoardSearchComponent, EventBoardDateFilterComponent, EventBoardDateRange, EventCardComponent, EventCardEntry, EventCardListComponent } from '@open-event/ui'
import { MatButton } from '@angular/material/button'
import { MatIcon } from '@angular/material/icon'
import { TranslatePipe } from '@ngx-translate/core'

@Component({
  selector: 'app-event-board',
  templateUrl: './event-board.component.html',
  imports: [BoardSearchComponent, EventBoardDateFilterComponent, EventCardListComponent, EventCardComponent, MatButton, MatIcon, TranslatePipe]
})
export class EventBoardComponent {
  private service = inject(EventService)
  private responsive = inject(BreakpointObserver)
  private route = inject(ActivatedRoute)

  private routeKey = toSignal(this.route.paramMap.pipe(map(p => p.get('id') ?? '')), { initialValue: '' })

  readonly mobileView = toSignal(this.responsive.observe(['(min-width: 768px)']).pipe(map((s) => !s.matches)), { initialValue: false })
  readonly filterVisible = signal(false)
  readonly showAvailableOnly = signal(false)
  readonly showHistory = signal(false)

  private query = signal('')
  private fromDate = signal<string | undefined>(undefined)
  private toDate = signal<string | undefined>(undefined)
  private page = signal(0)
  private size = signal(200)
  private infiniteScrollMode = signal(false)

  private criteria = computed(() => ({
    key: this.routeKey(),
    request: new PublicEventSearchRequest(this.query(), this.fromDate(), this.toDate(), this.showAvailableOnly()),
    page: this.page(),
    size: this.size()
  }))

  private searchResource = resource({
    params: this.criteria,
    loader: (p) => toPromise(this.service.search(p.params.key, p.params.request, p.params.page, p.params.size), p.abortSignal)
  })

  readonly entries = signal<PublicEvent[]>([])
  readonly reloading = this.searchResource.isLoading
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

  toCardData(event: PublicEvent): EventCardEntry {
    return {
      link: ['/event', event.key],
      title: event.title,
      ownerName: event.owner.name,
      start: event.start,
      finish: event.finish,
      hasLocation: event.hasLocation,
      zip: event.zip,
      city: event.city,
      country: event.country,
      categories: event.categories,
      tags: event.tags
    }
  }

  private applyDefaultRange() {
    const start = this.showHistory() ? undefined : (DateTime.now().startOf('day').toISODate() ?? undefined)
    this.fromDate.set(start)
    this.toDate.set(undefined)
    this.page.set(0)
  }
}
