import { Component, EventEmitter, inject, OnInit } from '@angular/core'
import { defaultEventSearchRequest, EventRangePickerComponent, EventRangeSelection, EventSearchEntry, EventSearchResponse } from '@open-event/core'
import { EventService, ExportService } from '@open-event/admin'
import { download } from '@open-event/shared'
import { HotToastService } from '@ngxpert/hot-toast'
import { MatDialog } from '@angular/material/dialog'
import { debounceTime, distinctUntilChanged } from 'rxjs'
import { PageEvent } from '@angular/material/paginator'
import { EventChangeDialogComponent } from './event-change-dialog/event-change-dialog.component'
import { EventDeleteDialogComponent } from './event-delete-dialog/event-delete-dialog.component'
import { MatCard } from '@angular/material/card'
import { EventTableComponent } from './event-table/event-table.component'
import { BoardComponent } from '../../shared/board/board.component'
import { ReactiveFormsModule } from '@angular/forms'
import { MatButton } from '@angular/material/button'
import { MatIcon } from '@angular/material/icon'
import { MatProgressSpinner } from '@angular/material/progress-spinner'
import { MatDivider } from '@angular/material/divider'
import { DateTime } from 'luxon'
import { TranslatePipe, TranslateService } from '@ngx-translate/core'
import { Sort } from '@angular/material/sort'

@Component({
  selector: 'admin-event',
  imports: [MatCard, EventTableComponent, BoardComponent, ReactiveFormsModule, EventRangePickerComponent, MatIcon, MatProgressSpinner, MatDivider, TranslatePipe, MatButton],
  templateUrl: './event.component.html',
  styleUrl: './event.component.scss'
})
export class EventComponent implements OnInit {
  reloading = false
  exportingEvents = false
  exportingSummary = false
  pageSize = 20
  pageNumber = 0
  totalElements = 0
  showHistory = false
  sort: Sort | undefined = undefined
  data: EventSearchEntry[] = []
  keyUp = new EventEmitter<string>()
  request = defaultEventSearchRequest()
  private service = inject(EventService)
  private exportService = inject(ExportService)
  private translateService = inject(TranslateService)
  private toast = inject(HotToastService)
  private dialog = inject(MatDialog)

  get fullTextSearch(): string {
    return this.request.fullTextSearch
  }

  set fullTextSearch(val: string) {
    if (this.request.fullTextSearch === val) return
    this.request.fullTextSearch = val
    this.search()
  }

  ngOnInit() {
    this.resetFilter()
    this.keyUp.pipe(debounceTime(500), distinctUntilChanged()).subscribe((query) => (this.fullTextSearch = query))
  }

  search() {
    this.load(0, this.pageSize)
  }

  handlePageChange(event: PageEvent) {
    if (this.reloading) return
    this.pageSize = event.pageSize
    this.load(event.pageIndex, event.pageSize)
  }

  handleSortChange(event: Sort) {
    if (this.reloading) return
    this.sort = event
    this.load(0, this.pageSize)
  }

  handleRangeChanged(event: EventRangeSelection) {
    this.request.from = event.from.toISODate() ?? ''
    this.request.to = event.to.toISODate() ?? ''
    this.search()
  }

  edit(entry: EventSearchEntry) {
    this.dialog
      .open(EventChangeDialogComponent, { width: '650px', data: entry })
      .afterClosed()
      .subscribe(() => this.search())
  }

  delete(entry: EventSearchEntry) {
    this.dialog
      .open(EventDeleteDialogComponent, { width: '650px', data: entry })
      .afterClosed()
      .subscribe(() => this.search())
  }

  toggleHistory() {
    this.showHistory = !this.showHistory
    this.request.from = this.showHistory ? '' : (DateTime.now().startOf('day').toISODate() ?? '')
    this.search()
  }

  resetFilter() {
    this.request = defaultEventSearchRequest()
    this.showHistory = false
    this.request.from = DateTime.now().startOf('day').toISODate() ?? ''
    this.search()
  }

  exportEvents() {
    if (this.exportingEvents) return
    this.exportingEvents = true
    this.exportService.exportEvents(this.request).subscribe({
      next: (r) => {
        download(r)
        this.exportingEvents = false
      },
      error: () => {
        this.exportingEvents = false
      }
    })
  }

  exportMail() {
    this.exportService.exportEventsToEmail(this.request).subscribe({
      next: () => this.translateService.get('backoffice.export.action.mail.success').subscribe((t) => this.toast.success(t)),
      error: () => this.translateService.get('backoffice.export.action.mail.error').subscribe((t) => this.toast.error(t))
    })
  }

  exportSummary() {
    if (this.exportingSummary) return
    this.exportingSummary = true
    this.exportService.exportSummary(this.request).subscribe({
      next: (r) => {
        download(r)
        this.exportingSummary = false
      },
      error: () => {
        this.exportingSummary = false
      }
    })
  }

  private load(page: number, size: number) {
    if (this.reloading) return
    this.reloading = true
    this.service.search(this.request, page, size, this.sort).subscribe({
      next: (value) => this.handleData(value),
      error: () => {
        this.toast.error()
        this.reloading = false
      }
    })
  }

  private handleData(response: EventSearchResponse) {
    const p = response.result
    this.data = p.content
    this.pageSize = p.pageable.size
    this.pageNumber = p.pageable.number
    this.totalElements = p.totalSize
    this.reloading = false
  }
}
