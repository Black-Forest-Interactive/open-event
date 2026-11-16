import { Component, input, computed, ChangeDetectionStrategy } from '@angular/core'
import { MatIcon } from '@angular/material/icon'
import { Event } from '@open-event/core'

@Component({
  selector: 'lib-event-published-icon',
  imports: [MatIcon],
  templateUrl: './event-published-icon.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './event-published-icon.component.scss'
})
export class EventPublishedIconComponent {
  event = input.required<Event>()

  readonly published = computed(() => this.event().published)
}
