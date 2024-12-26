import {Injectable} from '@angular/core';
import {BehaviorSubject} from "rxjs";
import {EventSearchEntry, EventSearchRequest, EventSearchResponse} from "../../search/model/search-api";
import {SearchService} from "../../search/model/search.service";
import {PageEvent} from "@angular/material/paginator";
import {FormControl, FormGroup} from "@angular/forms";

@Injectable({
  providedIn: 'root'
})
export class EventBoardService {

  reloading: BehaviorSubject<boolean> = new BehaviorSubject(false);
  searching: boolean = false
  pageSize: number = 50
  pageIndex: number = 0
  totalSize: number = 0
  hasMoreElements: boolean = false
  entries: EventSearchEntry[] = []
  request: EventSearchRequest = new EventSearchRequest('', undefined, undefined, false, false, false)
  infiniteScrollMode: boolean = false
  filterToolbarVisible: boolean = true

  range = new FormGroup({
    start: new FormControl<Date | null>(null),
    end: new FormControl<Date | null>(null),
  })


  constructor(private searchService: SearchService) {
    this.updateRange(null, null)
  }

  handleRangeChanged() {
    let value = this.range.value
    this.updateRange(value.start, value.end)
  }

  updateRange(start: Date | null | undefined, end: Date | null | undefined) {
    this.range.setValue({
      start: start ?? null,
      end: end ?? null
    })
    let startDate = null
    if (start) {
      startDate = new Date(start)
      startDate.setUTCHours(0, 0, 0, 0)
    } else if (!this._showHistory) {
      startDate = new Date()
      startDate.setUTCHours(0, 0, 0, 0)
    }

    let endDate = null
    if (end) {
      endDate = new Date(end)
      endDate.setUTCHours(23, 59, 59, 999)
    }


    let from = startDate?.toISOString()
    let to = endDate?.toISOString()
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
    this.updateRange(null, null)
  }


  onScroll() {
    if (this.reloading.value) return
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
    if (this.reloading.value) return console.log("Ignore reload " + page + ":" + size + " cause reloading ongoing")
    this.reloading.next(true)
    this.searchService.searchEvents(this.request, page, size).subscribe(
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
    this.reloading.next(false)
    this.searching = false
  }

  private handleError(err: any) {
    console.error("Failed to load data", err)
    this.reloading.next(false)
  }

}

export interface RangeFilter {
  start: Date | null | undefined
  end: Date | null | undefined
}
