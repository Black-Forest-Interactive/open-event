import { Component, computed, effect, inject, signal } from '@angular/core'
import { BreakpointObserver } from '@angular/cdk/layout'
import { toSignal } from '@angular/core/rxjs-interop'
import { map } from 'rxjs'
import { EventBoardService } from '../event-board.service'
import { EventBoardListComponent } from '../event-board-list/event-board-list.component'
import { EventBoardCalendarComponent } from '../event-board-calendar/event-board-calendar.component'
import { EventBoardTableComponent } from '../event-board-table/event-board-table.component'
import { EventBoardFilterComponent } from '../event-board-filter/event-board-filter.component'
import { EventBoardMapComponent } from '../event-board-map/event-board-map.component'
import { EventBoardHeaderComponent } from '../event-board-header/event-board-header.component'

@Component({
  selector: 'portal-event-board',
  templateUrl: './event-board.component.html',
  styleUrl: './event-board.component.scss',
  imports: [EventBoardListComponent, EventBoardCalendarComponent, EventBoardTableComponent, EventBoardFilterComponent, EventBoardMapComponent, EventBoardHeaderComponent],
  standalone: true
})
export class EventBoardComponent {
  private service = inject(EventBoardService)
  private responsive = inject(BreakpointObserver)

  readonly mobileView = toSignal(this.responsive.observe(['(min-width: 1000px)']).pipe(map(state => !state.matches)), { initialValue: false })
  readonly mode = signal('list')
  readonly filterVisible = computed(() => !this.mobileView())

  constructor() {
    effect(() => {
      const mobile = this.mobileView()
      this.service.filterToolbarVisible = !mobile
      this.service.infiniteScrollMode = mobile
    })
  }

  setMode(mode: string) {
    this.mode.set(mode)
  }
}
