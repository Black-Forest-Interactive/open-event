import { Component, inject } from '@angular/core'
import { EventBoardService } from '../event-board.service'
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
  protected service = inject(EventBoardService)
}
