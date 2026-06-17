import { Component, computed, effect, inject, TemplateRef, viewChild } from '@angular/core'
import { BreakpointObserver } from '@angular/cdk/layout'
import { toSignal } from '@angular/core/rxjs-interop'
import { map } from 'rxjs'
import { EventBoardService } from '../event-board.service'
import { EventBoardListComponent } from '../event-board-list/event-board-list.component'
import { EventBoardCalendarComponent } from '../event-board-calendar/event-board-calendar.component'
import { EventBoardFilterComponent } from '../event-board-filter/event-board-filter.component'
import { EventBoardMapComponent } from '../event-board-map/event-board-map.component'
import { EventBoardNavbarComponent } from '../event-board-navbar/event-board-navbar.component'
import { BoardSearchComponent } from '@open-event/ui'
import { LoadingBarComponent } from '@open-event/shared'
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
  standalone: true
})
export class EventBoardComponent {
  protected service = inject(EventBoardService)
  readonly reloading = this.service.reloading
  private responsive = inject(BreakpointObserver)
  private bottomSheet = inject(MatBottomSheet)
  private filterSheet = viewChild<TemplateRef<unknown>>('filterSheet')
  readonly mobileView = toSignal(this.responsive.observe(['(min-width: 768px)']).pipe(map((s) => !s.matches)), { initialValue: false })

  readonly whenLabelKey = computed(() => {
    switch (this.service.preselection()) {
      case 'today':
        return 'event.filter.when.today'
      case 'weekend':
        return 'event.filter.when.weekend'
      case 'next_week':
        return 'event.filter.when.nextWeek'
      default:
        return ''
    }
  })

  readonly activeFilterCount = computed(() => {
    const preselection = this.service.preselection()
    let count = this.service.categoryFilter().size
    if (preselection !== undefined && preselection !== 'any') count++
    if (this.service.showAvailableOnly()) count++
    return count
  })

  constructor() {
    effect(() => {
      const mobile = this.mobileView()
      this.service.setFilterToolbarVisible(!mobile)
      this.service.setInfiniteScrollMode(mobile)
    })
  }

  setQuery(query: string) {
    this.service.setQuery(query)
  }

  openFilter() {
    const sheet = this.filterSheet()
    if (sheet) this.bottomSheet.open(sheet)
  }

  removeWhenFilter() {
    this.service.handlePreselectionChanged(true, 'any')
  }

  removeCategoryFilter(name: string) {
    this.service.toggleCategory(name)
  }

  removeAvailableFilter() {
    this.service.toggleAvailableEvents()
  }
}
