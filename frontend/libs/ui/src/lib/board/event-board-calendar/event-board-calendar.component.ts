import { Component, computed, input } from '@angular/core'
import { DatePipe } from '@angular/common'
import { TranslatePipe } from '@ngx-translate/core'
import { MatIcon } from '@angular/material/icon'
import { LoadingBarComponent } from '@open-event/shared'
import { EventBoardRowComponent } from '../event-row/event-row.component'
import { EventBoardEntry } from '../event-board.api'

@Component({
  selector: 'lib-event-board-calendar',
  templateUrl: './event-board-calendar.component.html',
  imports: [LoadingBarComponent, EventBoardRowComponent, MatIcon, DatePipe, TranslatePipe]
})
export class EventBoardCalendarComponent {
  entries = input.required<EventBoardEntry[]>()
  reloading = input(false)

  readonly groups = computed(() => {
    const days = new Map<string, EventBoardEntry[]>()
    for (const entry of this.entries()) {
      const key = entry.start.substring(0, 10)
      const list = days.get(key)
      if (list) list.push(entry)
      else days.set(key, [entry])
    }
    return Array.from(days.entries())
      .sort(([a], [b]) => a.localeCompare(b))
      .map(([date, entries]) => ({ date, entries }))
  })
}
