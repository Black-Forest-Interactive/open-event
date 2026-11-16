import { Component, computed, input, ChangeDetectionStrategy } from '@angular/core'
import { EventContact, EventInfo } from '@open-event/core'
import { AccountDisplayNamePipe, AvatarComponent } from '@open-event/ui'
import { TranslatePipe } from '@ngx-translate/core'
import { MatIcon } from '@angular/material/icon'

@Component({
  selector: 'portal-event-host-block',
  templateUrl: './event-host-block.component.html',
  imports: [AvatarComponent, AccountDisplayNamePipe, TranslatePipe, MatIcon],
  changeDetection: ChangeDetectionStrategy.Eager
})
export class EventHostBlockComponent {
  info = input.required<EventInfo>()
  contact = input<EventContact>()

  readonly owner = computed(() => this.info().event.owner)
  readonly phone = computed(() => this.contact()?.phone ?? '')
  readonly mobile = computed(() => this.contact()?.mobile ?? '')
}
