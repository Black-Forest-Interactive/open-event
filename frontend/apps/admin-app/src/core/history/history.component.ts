import { Component, computed, inject, resource, signal, ChangeDetectionStrategy } from '@angular/core'
import { DatePipe } from '@angular/common'
import { HistoryService } from '@open-event/admin'
import { toPromise } from '@open-event/shared'
import { MatCard } from '@angular/material/card'
import { MatIconModule } from '@angular/material/icon'
import { MatChipsModule } from '@angular/material/chips'
import { MatPaginator, PageEvent } from '@angular/material/paginator'
import { TranslatePipe } from '@ngx-translate/core'
import { BoardComponent } from '../../shared/board/board.component'
import { BoardSearchComponent } from '@open-event/ui'
import { HistoryTableComponent } from './history-table/history-table.component'

@Component({
  selector: 'admin-history',
  imports: [MatCard, MatIconModule, MatChipsModule, MatPaginator, TranslatePipe, DatePipe, BoardComponent, BoardSearchComponent, HistoryTableComponent],
  templateUrl: './history.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './history.component.scss'
})
export class HistoryComponent {
  readonly FILTERS = ['all', 'EVENT_CREATED', 'EVENT_CHANGED', 'EVENT_DELETED', 'PARTICIPANT_STATUS_CHANGED']

  private service = inject(HistoryService)

  private page = signal(0)
  private size = signal(25)
  readonly eventQuery = signal('')
  private criteria = computed(() => ({ page: this.page(), size: this.size(), search: this.eventQuery() }))
  private eventsResource = resource({
    params: this.criteria,
    loader: (p) => toPromise(this.service.getAllHistoryEventInfos(p.params.page, p.params.size, p.params.search), p.abortSignal)
  })
  private result = computed(() => this.eventsResource.value())

  readonly events = computed(() => this.result()?.content ?? [])
  readonly pageNumber = computed(() => this.result()?.pageable.number ?? 0)
  readonly pageSize = computed(() => this.result()?.pageable.size ?? 25)
  readonly totalElements = computed(() => this.result()?.totalSize ?? 0)
  readonly reloading = this.eventsResource.isLoading

  private selectedEventId = signal<number | undefined>(undefined)
  readonly selected = computed(() => this.events().find((e) => e.event.id === this.selectedEventId()) ?? this.events()[0])

  readonly entryQuery = signal('')
  readonly typeFilter = signal('all')
  readonly filteredEntries = computed(() => {
    const sel = this.selected()
    if (!sel) return []
    const type = this.typeFilter()
    const q = this.entryQuery().trim().toLowerCase()
    return sel.entries.filter((e) => {
      if (type !== 'all' && e.type !== type) return false
      if (!q) return true
      return e.message.toLowerCase().includes(q) || e.info.toLowerCase().includes(q) || e.actor.name.toLowerCase().includes(q)
    })
  })

  selectEvent(id: number) {
    this.selectedEventId.set(id)
    this.entryQuery.set('')
    this.typeFilter.set('all')
  }

  setEventQuery(query: string) {
    this.page.set(0)
    this.eventQuery.set(query)
  }

  setEntryQuery(query: string) {
    this.entryQuery.set(query)
  }

  setTypeFilter(type: string) {
    this.typeFilter.set(type)
  }

  reload() {
    this.eventsResource.reload()
  }

  handlePageChange(event: PageEvent) {
    this.size.set(event.pageSize)
    this.page.set(event.pageIndex)
  }
}
