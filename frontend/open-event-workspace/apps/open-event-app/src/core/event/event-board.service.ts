import {Injectable, signal} from '@angular/core';
import {PageEvent} from "@angular/material/paginator";
import {FormControl, FormGroup} from "@angular/forms";
import {DateTime} from "luxon";
import {EventSearchEntry, EventSearchRequest, EventSearchResponse} from "@open-event-workspace/core";
import {EventService} from "@open-event-workspace/app";

@Injectable({
  providedIn: 'root'
})
export class EventBoardService {

  reloading = signal(false)
  searching: boolean = false
  pageSize: number = 200
  pageIndex: number = 0
  totalSize: number = 0
  hasMoreElements: boolean = false
  entries: EventSearchEntry[] = []
  request: EventSearchRequest = new EventSearchRequest('', undefined, undefined, false, false, false)
  infiniteScrollMode: boolean = false
  filterToolbarVisible: boolean = true
  preselection: string | undefined

  range = new FormGroup({
    start: new FormControl<DateTime | null>(null),
    end: new FormControl<DateTime | null>(null),
  })


  constructor(private service: EventService) {
    this.updateRange(null, null)
  }


  handleDatePickerChanged() {
    this.preselection = undefined
    this.handleRangeChanged()
  }

  handlePreselectionChanged(selected: boolean, value: any) {
    if (this.preselection === value) return
    this.preselection = value

    if (!selected) {
      this.updateRange(null, null)
    } else if (value === 'today') {
      this.selectToday()
    } else if (value === 'this_week') {
      this.selectThisWeek()
    } else if (value === 'next_week') {
      this.selectNextWeek()
    }
  }

  private selectToday() {
    let now = DateTime.now()
    this.updateRange(now, now)
  }

  private selectThisWeek() {
    let now = DateTime.now()
    let start = now.startOf('week')
    let end = now.endOf('week')
    this.updateRange(start, end)
  }

  private selectNextWeek() {
    let now = DateTime.now()

    let start = now.plus({weeks: 1}).startOf('week')

    let end = now.plus({weeks: 1}).endOf('week')
    this.updateRange(start, end)
  }

  private handleRangeChanged() {
    let value = this.range.value
    let start = value.start
    let end = value.end
    this.updateRange(start, end)
  }

  private updateRange(start: DateTime | null | undefined, end: DateTime | null | undefined) {
    this.range.setValue({
      start: start ?? null,
      end: end ?? null
    })
    console.log(`[${new Date().toISOString()}] update range ${JSON.stringify(this.range.value)}`)
    let startDate = null
    if (start) {
      startDate = start.startOf('day')
    } else if (!this._showHistory) {
      startDate = DateTime.now().startOf('day')
    }

    let endDate = null
    if (end) {
      endDate = end.endOf('day')
    }


    let from = startDate?.toISODate() ?? undefined
    let to = endDate?.toISODate() ?? undefined
    if (this.request.from === from && this.request.to === to) return
    this.request.from = from
    this.request.to = to
    this.search()
  }

  set fullTextSearch(val: string) {
    if (this.request.fullTextSearch === val) return
    this.request.fullTextSearch = val
    this.search()
  }

  get fullTextSearch(): string {
    return this.request.fullTextSearch
  }

  private _showHistory: boolean = false
  set showHistory(val: boolean) {
    if (this._showHistory == val) return
    this._showHistory = val
    this.handleRangeChanged()
  }

  get showHistory(): boolean {
    return this._showHistory
  }

  set ownEvents(val: boolean) {
    if (this.request.ownEvents == val) return
    this.request.ownEvents = val
    this.search()
  }

  get ownEvents(): boolean {
    return this.request.ownEvents
  }

  set availableEvents(val: boolean) {
    if (this.request.onlyAvailableEvents == val) return
    this.request.onlyAvailableEvents = val
    this.search()
  }

  get availableEvents(): boolean {
    return this.request.onlyAvailableEvents
  }

  set participatingEvents(val: boolean) {
    if (this.request.participatingEvents == val) return
    this.request.participatingEvents = val
    this.search()
  }

  get participatingEvents(): boolean {
    return this.request.participatingEvents
  }


  resetFilter() {
    this.request = new EventSearchRequest('', undefined, undefined, false, false, false)
    this._showHistory = false
    this.preselection = undefined
    this.updateRange(null, null)
  }


  onScroll() {
    if (this.reloading()) return
    if (!this.hasMoreElements) return
    this.reload(this.pageIndex + 1, this.pageSize)
  }

  handlePageChange(event: PageEvent) {
    this.reload(event.pageIndex, event.pageSize)
  }

  search() {
    this.reload(0, this.pageSize)
  }

  private reload(page: number, size: number) {
    if (this.reloading()) return console.log("Ignore reload " + page + ":" + size + " cause reloading ongoing")
    this.reloading.set(true)
    this.service.search(this.request, page, size).subscribe(
      {
        next: value => this.handleData(value),
        error: e => this.handleError(e)
      }
    )
  }

  private handleData(response: EventSearchResponse) {
    let value = response.result
    if (this.infiniteScrollMode) {
      this.entries.push(...value.content)
    } else {
      this.entries = value.content
    }
    this.pageSize = value.pageable.size
    this.pageIndex = value.pageable.number
    this.totalSize = value.totalSize
    this.hasMoreElements = value.content.length != 0 && this.pageIndex != (value.totalPages - 1)
    this.reloading.set(false)
    this.searching = false
  }

  private handleError(err: any) {
    console.error("Failed to load data", err)
    this.reloading.set(false)
  }

}

export interface RangeFilter {
  start: Date | null | undefined
  end: Date | null | undefined
}
