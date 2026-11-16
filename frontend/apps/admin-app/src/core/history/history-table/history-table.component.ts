import { Component, input, output, ChangeDetectionStrategy } from '@angular/core'
import { MatTableModule } from '@angular/material/table'
import { TranslatePipe } from '@ngx-translate/core'
import { DatePipe } from '@angular/common'
import { HistoryEntry } from '@open-event/core'
import { AccountDisplayNamePipe, AvatarComponent } from '@open-event/ui'
import { MatPaginator, PageEvent } from '@angular/material/paginator'
import { MatIconModule } from '@angular/material/icon'
import { MatChipsModule } from '@angular/material/chips'
import { MatTooltipModule } from '@angular/material/tooltip'

@Component({
  selector: 'admin-history-table',
  imports: [MatTableModule, TranslatePipe, DatePipe, AccountDisplayNamePipe, AvatarComponent, MatPaginator, MatIconModule, MatChipsModule, MatTooltipModule],
  templateUrl: './history-table.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './history-table.component.scss'
})
export class HistoryTableComponent {
  data = input.required<HistoryEntry[]>()
  reloading = input.required<boolean>()
  pageNumber = input.required<number>()
  pageSize = input.required<number>()
  totalElements = input.required<number>()

  pageEvent = output<PageEvent>()
  displayedColumns: string[] = ['timestamp', 'type', 'message', 'actor', 'info']

  typeIcon(type: string): string {
    switch (type) {
      case 'EVENT_CREATED':
        return 'add_circle'
      case 'EVENT_CHANGED':
        return 'edit'
      case 'EVENT_DELETED':
        return 'delete'
      case 'PARTICIPANT_STATUS_CHANGED':
        return 'group'
      default:
        return 'info'
    }
  }

  typeClass(type: string): string {
    const base = 'transition-all'
    switch (type) {
      case 'EVENT_CREATED':
        return `${base} !bg-green-100 !text-green-700`
      case 'EVENT_CHANGED':
        return `${base} !bg-orange-100 !text-orange-700`
      case 'EVENT_DELETED':
        return `${base} !bg-red-100 !text-red-700`
      case 'PARTICIPANT_STATUS_CHANGED':
        return `${base} !bg-blue-100 !text-blue-700`
      default:
        return base
    }
  }
}
