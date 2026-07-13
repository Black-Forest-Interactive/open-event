import { Component, DestroyRef, effect, inject, TemplateRef, viewChild, ChangeDetectionStrategy } from '@angular/core'
import { BreakpointObserver } from '@angular/cdk/layout'
import { toSignal } from '@angular/core/rxjs-interop'
import { map } from 'rxjs'
import { computed } from '@angular/core'
import { EventBoardService } from '../event-board.service'
import { EventBoardListComponent } from '../event-board-list/event-board-list.component'
import { EventBoardCalendarComponent } from '../event-board-calendar/event-board-calendar.component'
import { EventBoardFilterComponent } from '../event-board-filter/event-board-filter.component'
import { EventBoardMapComponent } from '../event-board-map/event-board-map.component'
import { EventBoardNavbarComponent } from '../event-board-navbar/event-board-navbar.component'
import { BoardSearchComponent } from '@open-event/ui'
import { LoadingBarComponent, TourService } from '@open-event/shared'
import { eventBoardTour } from './event-board.tour'
import { MatButton, MatIconButton } from '@angular/material/button'
import { MatIcon } from '@angular/material/icon'
import { MatBadge } from '@angular/material/badge'
import { MatButtonToggle, MatButtonToggleGroup } from '@angular/material/button-toggle'
import { MatBottomSheet } from '@angular/material/bottom-sheet'
import { RouterLink } from '@angular/router'
import { TranslatePipe } from '@ngx-translate/core'

@Component({
  selector: 'portal-event-board',
  templateUrl: './event-board.component.html',
  styleUrl: './event-board.component.scss',
  imports: [
    EventBoardListComponent,
    EventBoardCalendarComponent,
    EventBoardFilterComponent,
    EventBoardMapComponent,
    EventBoardNavbarComponent,
    BoardSearchComponent,
    LoadingBarComponent,
    MatButton,
    MatIconButton,
    MatIcon,
    MatBadge,
    MatButtonToggleGroup,
    MatButtonToggle,
    RouterLink,
    TranslatePipe
  ],
  changeDetection: ChangeDetectionStrategy.Eager,
  standalone: true
})
export class EventBoardComponent {
  protected service = inject(EventBoardService)
  readonly reloading = this.service.reloading
  private responsive = inject(BreakpointObserver)
  private bottomSheet = inject(MatBottomSheet)
  private tourService = inject(TourService)
  private destroyRef = inject(DestroyRef)
  private filterSheet = viewChild<TemplateRef<unknown>>('filterSheet')
  readonly mobileView = toSignal(this.responsive.observe(['(min-width: 768px)']).pipe(map((s) => !s.matches)), { initialValue: false })

  readonly activeFilterCount = computed(() => {
    let count = this.service.categoryFilter().size
    if (this.service.showAvailableOnly()) count++
    return count
  })

  constructor() {
    effect(() => {
      this.service.setInfiniteScrollMode(this.mobileView())
    })
    this.service.reload()
    this.tourService.register(eventBoardTour, () => !this.reloading())
    this.destroyRef.onDestroy(() => this.tourService.unregister(eventBoardTour.id))
  }

  setQuery(query: string) {
    this.service.setQuery(query)
  }

  openFilter() {
    const sheet = this.filterSheet()
    if (sheet) this.bottomSheet.open(sheet)
  }
}
