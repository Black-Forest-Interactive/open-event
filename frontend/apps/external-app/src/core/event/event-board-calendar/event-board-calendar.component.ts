import { Component, computed, input } from '@angular/core'
import { PublicEvent } from '@open-event/external'
import { EventBoardCalendarComponent as LibEventBoardCalendarComponent } from '@open-event/ui'
import { toEventBoardEntry } from '../event-board-entry.mapper'

@Component({
  selector: 'app-event-board-calendar',
  template: '<lib-event-board-calendar [entries]="mapped()"></lib-event-board-calendar>',
  imports: [LibEventBoardCalendarComponent]
})
export class EventBoardCalendarComponent {
  entries = input.required<PublicEvent[]>()
  readonly mapped = computed(() => this.entries().map(e => toEventBoardEntry(e)))
}
