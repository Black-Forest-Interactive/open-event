import { Component, computed, input, resource, signal, inject } from '@angular/core'
import { EventInfo } from '@open-event/core'
import { EventService } from '@open-event/admin'
import { LoadingBarComponent, toPromise } from '@open-event/shared'
import { HistoryTableComponent } from '../../history/history-table/history-table.component'
import { PageEvent } from '@angular/material/paginator'
import { BoardCardComponent } from '../../../shared/board-card/board-card.component'

@Component({
  selector: 'admin-event-details-history',
  imports: [LoadingBarComponent, HistoryTableComponent, BoardCardComponent],
  templateUrl: './event-details-history.component.html',
  styleUrl: './event-details-history.component.scss'
})
export class EventDetailsHistoryComponent {
  private service = inject(EventService)

  event = input.required<EventInfo>()
  page = signal(0)
  size = signal(20)

  readonly historyCriteria = computed(() => ({
    data: this.event(),
    page: this.page(),
    size: this.size()
  }))

  private historyResource = resource({
    params: this.historyCriteria,
    loader: (param) => toPromise(this.service.getEventHistory(param.params.data.event.id, param.params.page, param.params.size), param.abortSignal)
  })

  readonly result = computed(this.historyResource.value ?? undefined)
  readonly history = computed(() => this.result()?.content ?? [])
  readonly totalSize = computed(() => this.result()?.totalSize ?? 0)
  readonly loading = this.historyResource.isLoading
  readonly error = this.historyResource.error

  handlePageChange($event: PageEvent) {
    this.page.set($event.pageIndex)
    this.size.set($event.pageSize)
  }
}
