import { Component, computed, input } from '@angular/core'
import { EventSearchEntry } from '@open-event/core'
import { EventBoardRowComponent } from '@open-event/ui'
import { toEventBoardEntry } from '../event-board-entry.mapper'

@Component({
  selector: 'portal-event-row',
  template: '<lib-event-board-row [data]="mapped()"></lib-event-board-row>',
  imports: [EventBoardRowComponent],
  standalone: true
})
export class EventRowComponent {
  entry = input.required<EventSearchEntry>()
  readonly mapped = computed(() => toEventBoardEntry(this.entry()))
}
