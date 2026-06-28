import { Component, computed, input, output } from '@angular/core'
import { EventSearchEntry } from '@open-event/core'
import { EventBoardListComponent as LibEventBoardListComponent, EventBoardCalendarComponent } from '@open-event/ui'
import { MatIcon } from '@angular/material/icon'
import { TranslatePipe } from '@ngx-translate/core'
import { toEventBoardEntry } from '../event-board-entry.mapper'

@Component({
  selector: 'portal-event-board-list',
  templateUrl: './event-board-list.component.html',
  imports: [LibEventBoardListComponent, EventBoardCalendarComponent, MatIcon, TranslatePipe],
  standalone: true
})
export class EventBoardListComponent {
  entries = input.required<EventSearchEntry[]>()
  reloading = input.required<boolean>()
  layout = input.required<'cards' | 'rows' | 'calendar' | 'map'>()
  navView = input.required<'all' | 'saved' | 'regs' | 'own'>()
  hasMoreElements = input.required<boolean>()
  nearEnd = output<void>()

  readonly mapped = computed(() => this.entries().map(e => toEventBoardEntry(e)))
  readonly listLayout = computed(() => this.layout() === 'rows' ? 'rows' as const : 'cards' as const)
}
