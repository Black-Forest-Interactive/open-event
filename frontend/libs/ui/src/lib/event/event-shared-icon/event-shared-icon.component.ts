import { Component, input, ChangeDetectionStrategy } from '@angular/core'
import { EventInfo } from '@open-event/core'
import { MatIcon } from '@angular/material/icon'

@Component({
  selector: 'lib-event-shared-icon',
  imports: [MatIcon],
  templateUrl: './event-shared-icon.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './event-shared-icon.component.scss'
})
export class EventSharedIconComponent {
  event = input.required<EventInfo>()
}
