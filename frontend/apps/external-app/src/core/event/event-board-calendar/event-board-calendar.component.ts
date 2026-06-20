import { Component, computed, input } from '@angular/core'
import { PublicEvent } from '@open-event/external'
import { DatePipe } from '@angular/common'
import { TranslatePipe } from '@ngx-translate/core'
import { EventRowComponent } from '../event-row/event-row.component'

@Component({
  selector: 'app-event-board-calendar',
  templateUrl: './event-board-calendar.component.html',
  imports: [DatePipe, TranslatePipe, EventRowComponent]
})
export class EventBoardCalendarComponent {
  entries = input.required<PublicEvent[]>()

  readonly groups = computed(() => {
    const map = new Map<string, PublicEvent[]>()
    for (const entry of this.entries()) {
      const date = entry.start.substring(0, 10)
      const list = map.get(date) ?? []
      list.push(entry)
      map.set(date, list)
    }
    return [...map.entries()].sort((a, b) => a[0].localeCompare(b[0])).map(([date, list]) => ({ date, entries: list }))
  })
}
