import { Component, computed, DestroyRef, effect, inject, resource, signal, ChangeDetectionStrategy } from '@angular/core'
import { BreakpointObserver } from '@angular/cdk/layout'
import { toSignal } from '@angular/core/rxjs-interop'
import { map } from 'rxjs'
import { DateTime } from 'luxon'
import { EventSearchEntry, EventSearchRequest } from '@open-event/core'
import { EventService } from '@open-event/portal'
import { BoardSearchComponent } from '@open-event/ui'
import { LoadingBarComponent, toPromise, TourService } from '@open-event/shared'
import { TranslatePipe } from '@ngx-translate/core'
import { EventBoardListComponent } from '../event-board-list/event-board-list.component'
import { EventBoardNavbarComponent } from '../event-board-navbar/event-board-navbar.component'
import { eventBoardRegsTour } from './event-board-regs.tour'

@Component({
  selector: 'portal-event-board-regs',
  templateUrl: './event-board-regs.component.html',
  imports: [EventBoardListComponent, EventBoardNavbarComponent, BoardSearchComponent, LoadingBarComponent, TranslatePipe],
  changeDetection: ChangeDetectionStrategy.Eager
})
export class EventBoardRegsComponent {
  private eventService = inject(EventService)
  private responsive = inject(BreakpointObserver)
  private tourService = inject(TourService)
  private destroyRef = inject(DestroyRef)

  readonly mobileView = toSignal(this.responsive.observe(['(min-width: 768px)']).pipe(map((s) => !s.matches)), { initialValue: false })

  private query = signal('')
  private fromDate = DateTime.now().startOf('day').toISODate() ?? undefined
  private page = signal(0)
  private size = signal(200)
  private infiniteScrollMode = signal(false)

  private criteria = computed(() => ({
    request: new EventSearchRequest(this.query(), this.fromDate, undefined, false, true, false, [], false, false, []),
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
    this.tourService.register(eventBoardRegsTour, () => !this.reloading())
    this.destroyRef.onDestroy(() => this.tourService.unregister(eventBoardRegsTour.id))
  }

  setQuery(val: string) {
    if (this.query() === val) return
    this.query.set(val)
    this.page.set(0)
  }

  onScroll() {
    if (this.reloading() || !this.hasMoreElements()) return
    this.page.set(this.pageIndex() + 1)
  }
}
