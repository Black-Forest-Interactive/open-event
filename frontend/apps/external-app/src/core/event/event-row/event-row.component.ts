import { Component, computed, input } from '@angular/core'
import { PublicEvent } from '@open-event/external'
import { EventBoardRowComponent } from '@open-event/ui'
import { toEventBoardEntry } from '../event-board-entry.mapper'

@Component({
  selector: 'app-event-row',
  template: '<lib-event-board-row [data]="mapped()"></lib-event-board-row>',
  imports: [EventBoardRowComponent]
})
export class EventRowComponent {
  event = input.required<PublicEvent>()
  readonly mapped = computed(() => toEventBoardEntry(this.event()))
}
