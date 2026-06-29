import { Component, computed, inject, input, output } from '@angular/core'
import { DatePipe } from '@angular/common'
import { RouterLink } from '@angular/router'
import { MatBottomSheet } from '@angular/material/bottom-sheet'
import { MatDialog } from '@angular/material/dialog'
import { MatButton, MatIconButton } from '@angular/material/button'
import { MatIcon } from '@angular/material/icon'
import { TranslatePipe } from '@ngx-translate/core'
import { EventSearchEntry } from '@open-event/core'
import { MatProgressBar } from '@angular/material/progress-bar'
import { CategoryChipComponent, EventBoardListComponent as LibEventBoardListComponent, EventBoardCalendarComponent, getCategoryStyle } from '@open-event/ui'
import { EventBroadcastSheetComponent } from '../../announcement/event-broadcast-sheet/event-broadcast-sheet.component'
import { EventCancelDialogComponent } from '../event-cancel-dialog/event-cancel-dialog.component'
import { EventEditDialogComponent } from '../event-edit/event-edit-dialog.component'
import { EventShareSheetComponent } from '../../share/event-share-sheet/event-share-sheet.component'
import { toEventBoardEntry } from '../event-board-entry.mapper'

@Component({
  selector: 'portal-event-board-list',
  templateUrl: './event-board-list.component.html',
  styleUrl: './event-board-list.component.scss',
  imports: [LibEventBoardListComponent, EventBoardCalendarComponent, DatePipe, RouterLink, MatButton, MatIconButton, MatIcon, TranslatePipe, CategoryChipComponent, MatProgressBar],
  standalone: true
})
export class EventBoardListComponent {
  entries = input.required<EventSearchEntry[]>()
  reloading = input.required<boolean>()
  layout = input.required<'cards' | 'rows' | 'calendar' | 'map'>()
  navView = input.required<'all' | 'saved' | 'regs' | 'own'>()
  hasMoreElements = input.required<boolean>()
  nearEnd = output<void>()

  private bottomSheet = inject(MatBottomSheet)
  private dialog = inject(MatDialog)

  readonly mapped = computed(() => this.entries().map(e => toEventBoardEntry(e)))
  readonly listLayout = computed(() => this.layout() === 'rows' ? 'rows' as const : 'cards' as const)

  categoryStyle(entry: EventSearchEntry) {
    return getCategoryStyle(entry.categories[0] ?? '')
  }

  fillPct(entry: EventSearchEntry): number {
    if (!entry.maxGuestAmount) return 0
    return Math.round((entry.amountAccepted / entry.maxGuestAmount) * 100)
  }

  edit(entry: EventSearchEntry) {
    this.dialog.open(EventEditDialogComponent, { data: { id: entry.id }, width: '680px', maxWidth: '95vw', disableClose: true })
  }

  openBroadcast(entry: EventSearchEntry) {
    this.bottomSheet.open(EventBroadcastSheetComponent, { data: { eventId: entry.id, eventTitle: entry.title, participantCount: entry.amountAccepted } })
  }

  openShare(entry: EventSearchEntry) {
    this.bottomSheet.open(EventShareSheetComponent, { data: { eventId: entry.id, eventTitle: entry.title } })
  }

  openCancel(entry: EventSearchEntry) {
    this.dialog.open(EventCancelDialogComponent, { width: '400px', data: { event: { id: entry.id, title: entry.title }, participantCount: entry.amountAccepted } })
  }
}
