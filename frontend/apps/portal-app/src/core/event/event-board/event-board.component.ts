import { Component, effect, inject, signal } from '@angular/core'
import { BreakpointObserver } from '@angular/cdk/layout'
import { toSignal } from '@angular/core/rxjs-interop'
import { map } from 'rxjs'
import { EventBoardService } from '../event-board.service'
import { EventBoardListComponent } from '../event-board-list/event-board-list.component'
import { EventBoardCalendarComponent } from '../event-board-calendar/event-board-calendar.component'
import { EventBoardTableComponent } from '../event-board-table/event-board-table.component'
import { EventBoardFilterComponent } from '../event-board-filter/event-board-filter.component'
import { EventBoardMapComponent } from '../event-board-map/event-board-map.component'
import { BoardSearchComponent } from '@open-event/ui'
import { LoadingBarComponent } from '@open-event/shared'
import { MatButton, MatIconButton } from '@angular/material/button'
import { MatIcon } from '@angular/material/icon'
import { MatButtonToggle, MatButtonToggleGroup } from '@angular/material/button-toggle'
import { RouterLink } from '@angular/router'
import { TranslatePipe } from '@ngx-translate/core'
import { MatMenu, MatMenuItem, MatMenuTrigger } from '@angular/material/menu'

@Component({
  selector: 'portal-event-board',
  templateUrl: './event-board.component.html',
  styleUrl: './event-board.component.scss',
  imports: [
    EventBoardListComponent,
    EventBoardCalendarComponent,
    EventBoardTableComponent,
    EventBoardFilterComponent,
    EventBoardMapComponent,
    BoardSearchComponent,
    LoadingBarComponent,
    MatButton,
    MatIconButton,
    MatIcon,
    MatButtonToggleGroup,
    MatButtonToggle,
    RouterLink,
    TranslatePipe,
    MatMenu,
    MatMenuItem,
    MatMenuTrigger
  ],
  standalone: true
})
export class EventBoardComponent {
  readonly mode = signal('list')
  private service = inject(EventBoardService)
  readonly reloading = this.service.reloading
  private responsive = inject(BreakpointObserver)
  readonly mobileView = toSignal(this.responsive.observe(['(min-width: 768px)']).pipe(map((s) => !s.matches)), { initialValue: false })

  constructor() {
    effect(() => {
      const mobile = this.mobileView()
      this.service.setFilterToolbarVisible(!mobile)
      this.service.setInfiniteScrollMode(mobile)
    })
  }

  setMode(mode: string) {
    this.mode.set(mode)
  }
  setQuery(query: string) {
    this.service.setQuery(query)
  }
}
