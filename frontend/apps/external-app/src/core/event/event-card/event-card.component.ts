import { Component, computed, input } from '@angular/core'
import { PublicEvent } from '@open-event/external'
import { EventCardComponent as LibEventCardComponent } from '@open-event/ui'
import { toEventBoardEntry } from '../event-board-entry.mapper'

@Component({
  selector: 'app-event-card',
  template: '<lib-event-card [data]="mapped()"></lib-event-card>',
  imports: [LibEventCardComponent]
})
export class EventCardComponent {
  event = input.required<PublicEvent>()
  readonly mapped = computed(() => toEventBoardEntry(this.event()))
}
