import { Component, computed, input } from '@angular/core'
import { DatePipe } from '@angular/common'
import { TranslatePipe } from '@ngx-translate/core'
import { MatIcon } from '@angular/material/icon'
import { EventSearchEntry } from '@open-event/core'
import { EventRowComponent } from '../event-row/event-row.component'
import { LoadingBarComponent } from '@open-event/shared'

@Component({
  selector: 'portal-event-board-calendar',
  templateUrl: './event-board-calendar.component.html',
  imports: [LoadingBarComponent, EventRowComponent, MatIcon, DatePipe, TranslatePipe],
  standalone: true
})
export class EventBoardCalendarComponent {
  entries = input.required<EventSearchEntry[]>()
  reloading = input.required<boolean>()

  readonly groups = computed(() => {
    const days = new Map<string, EventSearchEntry[]>()
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
