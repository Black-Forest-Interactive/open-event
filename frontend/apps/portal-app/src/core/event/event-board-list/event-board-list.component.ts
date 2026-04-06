import { Component, inject, TemplateRef, ViewChild } from '@angular/core'
import { EventBoardService } from '../event-board.service'
import { EventBoardFilterComponent } from '../event-board-filter/event-board-filter.component'
import { EventBoardListEntryComponent } from '../event-board-list-entry/event-board-list-entry.component'
import { MatButton, MatIconButton } from '@angular/material/button'
import { TranslatePipe } from '@ngx-translate/core'
import { LoadingBarComponent, ScrollNearEndDirective } from '@open-event/shared'
import { MatIcon } from '@angular/material/icon'
import { MatBottomSheet } from '@angular/material/bottom-sheet'

@Component({
  selector: 'portal-event-board-list',
  templateUrl: './event-board-list.component.html',
  styleUrl: './event-board-list.component.scss',
  imports: [EventBoardFilterComponent, EventBoardListEntryComponent, MatButton, MatIconButton, TranslatePipe, ScrollNearEndDirective, MatIcon, LoadingBarComponent],
  standalone: true
})
export class EventBoardListComponent {
  readonly service = inject(EventBoardService)
  private bottomSheet = inject(MatBottomSheet)

  @ViewChild('filterSheet') filterSheet!: TemplateRef<any>

  openFilter() { this.bottomSheet.open(this.filterSheet) }
}
