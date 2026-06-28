import { Component, input, output } from '@angular/core'
import { MatButton } from '@angular/material/button'
import { TranslatePipe } from '@ngx-translate/core'
import { LoadingBarComponent, ScrollNearEndDirective } from '@open-event/shared'
import { EventCardComponent } from '../event-card/event-card.component'
import { EventBoardRowComponent } from '../event-row/event-row.component'
import { EventBoardEntry } from '../event-board.api'

@Component({
  selector: 'lib-event-board-list',
  templateUrl: './event-board-list.component.html',
  imports: [EventCardComponent, EventBoardRowComponent, MatButton, TranslatePipe, ScrollNearEndDirective, LoadingBarComponent]
})
export class EventBoardListComponent {
  entries = input.required<EventBoardEntry[]>()
  reloading = input.required<boolean>()
  layout = input.required<'cards' | 'rows'>()
  hasMoreElements = input.required<boolean>()
  nearEnd = output<void>()
}
