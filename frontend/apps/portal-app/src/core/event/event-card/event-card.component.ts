import { Component, computed, input } from '@angular/core'
import { EventSearchEntry } from '@open-event/core'
import { EventCardComponent as LibEventCardComponent } from '@open-event/ui'
import { toEventBoardEntry } from '../event-board-entry.mapper'

@Component({
  selector: 'portal-event-card',
  template: '<lib-event-card [data]="mapped()"></lib-event-card>',
  imports: [LibEventCardComponent],
  standalone: true
})
export class EventCardComponent {
  entry = input.required<EventSearchEntry>()
  readonly mapped = computed(() => toEventBoardEntry(this.entry()))
}
