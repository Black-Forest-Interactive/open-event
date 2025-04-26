import {Component, computed, input, resource, signal} from '@angular/core';
import {EventInfo} from "@open-event-workspace/core";
import {EventService} from "@open-event-workspace/backoffice";
import {LoadingBarComponent, toPromise} from "@open-event-workspace/shared";
import {HistoryTableComponent} from "../../history/history-table/history-table.component";
import {PageEvent} from "@angular/material/paginator";
import {BoardCardComponent} from "../../../shared/board-card/board-card.component";

@Component({
  selector: 'app-event-details-history',
  imports: [
    LoadingBarComponent,
    HistoryTableComponent,
    BoardCardComponent
  ],
  templateUrl: './event-details-history.component.html',
  styleUrl: './event-details-history.component.scss'
})
export class EventDetailsHistoryComponent {
  event = input.required<EventInfo>()
  page = signal(0)
  size = signal(20)

  historyCriteria = computed(() => ({
    data: this.event(),
    page: this.page(),
    size: this.size(),
  }));

  historyResource = resource({
    request: this.historyCriteria,
    loader: (param) => {
      return toPromise(this.service.getEventHistory(param.request.data.event.id, param.request.page, param.request.size))
    }
  })

  result = computed(this.historyResource.value ?? undefined)
  history = computed(() => this.result()?.content ?? [])
  totalSize = computed(() => this.result()?.totalSize ?? 0)
  loading = this.historyResource.isLoading
  error = this.historyResource.error

  constructor(private service: EventService) {
  }

  handlePageChange($event: PageEvent) {
    this.page.set($event.pageIndex)
    this.size.set($event.pageSize)
  }
}
