import { Component, computed, input, ChangeDetectionStrategy } from '@angular/core'
import { EventInfo } from '@open-event/core'
import { AccountDisplayNamePipe, AvatarComponent } from '@open-event/ui'
import { TranslatePipe } from '@ngx-translate/core'

@Component({
  selector: 'portal-event-host-block',
  templateUrl: './event-host-block.component.html',
  imports: [AvatarComponent, AccountDisplayNamePipe, TranslatePipe],
  changeDetection: ChangeDetectionStrategy.Eager
})
export class EventHostBlockComponent {
  info = input.required<EventInfo>()

  readonly owner = computed(() => this.info().event.owner)
}
