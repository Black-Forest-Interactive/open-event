import { Component, input, output } from '@angular/core'
import { EventSearchEntry } from '@open-event/core'
import { EventCardComponent } from '../event-card/event-card.component'
import { EventRowComponent } from '../event-row/event-row.component'
import { MatButton } from '@angular/material/button'
import { MatIcon } from '@angular/material/icon'
import { TranslatePipe } from '@ngx-translate/core'
import { LoadingBarComponent, ScrollNearEndDirective } from '@open-event/shared'

@Component({
  selector: 'portal-event-board-list',
  templateUrl: './event-board-list.component.html',
  styleUrl: './event-board-list.component.scss',
  imports: [EventCardComponent, EventRowComponent, MatButton, MatIcon, TranslatePipe, ScrollNearEndDirective, LoadingBarComponent],
  standalone: true
})
export class EventBoardListComponent {
  entries = input.required<EventSearchEntry[]>()
  reloading = input.required<boolean>()
  layout = input.required<'cards' | 'rows' | 'calendar' | 'map'>()
  navView = input.required<'all' | 'saved' | 'regs' | 'own'>()
  hasMoreElements = input.required<boolean>()
  nearEnd = output<void>()
}
