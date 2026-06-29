import { Component, computed, effect, inject, resource, signal } from '@angular/core'
import { BreakpointObserver } from '@angular/cdk/layout'
import { toSignal } from '@angular/core/rxjs-interop'
import { map } from 'rxjs'
import { EventSearchEntry, EventSearchRequest } from '@open-event/core'
import { EventService } from '@open-event/portal'
import { BoardSearchComponent } from '@open-event/ui'
import { LoadingBarComponent, toPromise } from '@open-event/shared'
import { MatIcon } from '@angular/material/icon'
import { MatButton } from '@angular/material/button'
import { TranslatePipe } from '@ngx-translate/core'
import { EventBoardListComponent } from '../event-board-list/event-board-list.component'
import { EventBoardNavbarComponent } from '../event-board-navbar/event-board-navbar.component'
import { RouterLink } from '@angular/router'

@Component({
  selector: 'portal-event-board-own',
  templateUrl: './event-board-own.component.html',
  imports: [
    EventBoardListComponent,
    EventBoardNavbarComponent,
    BoardSearchComponent,
    LoadingBarComponent,
    MatIcon,
    MatButton,
    RouterLink,
    TranslatePipe
  ],
  standalone: true
})
export class EventBoardOwnComponent {
  private eventService = inject(EventService)
  private responsive = inject(BreakpointObserver)

  private readonly mobileView = toSignal(this.responsive.observe(['(min-width: 768px)']).pipe(map((s) => !s.matches)), { initialValue: false })

  private query = signal('')
  private page = signal(0)
  private size = signal(200)
  private infiniteScrollMode = signal(false)

  private criteria = computed(() => ({
    request: new EventSearchRequest(this.query(), undefined, undefined, true, false, false, [], false, false, []),
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
