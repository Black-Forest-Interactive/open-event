import { Component, computed, input } from '@angular/core'
import { EventSearchEntry } from '@open-event/core'
import { EventBoardCalendarComponent as LibEventBoardCalendarComponent } from '@open-event/ui'
import { toEventBoardEntry } from '../event-board-entry.mapper'

@Component({
  selector: 'portal-event-board-calendar',
  templateUrl: './event-board-calendar.component.html',
  imports: [LibEventBoardCalendarComponent],
  standalone: true
})
export class EventBoardCalendarComponent {
  entries = input.required<EventSearchEntry[]>()
  reloading = input.required<boolean>()

  readonly mapped = computed(() => this.entries().map(e => toEventBoardEntry(e)))
}
