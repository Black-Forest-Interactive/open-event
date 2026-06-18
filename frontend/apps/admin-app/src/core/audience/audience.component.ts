import { Component, EventEmitter, inject, OnInit } from '@angular/core'

import { MatCardModule } from '@angular/material/card'
import { MatIconModule } from '@angular/material/icon'
import { MatToolbarModule } from '@angular/material/toolbar'
import { TranslatePipe } from '@ngx-translate/core'
import { AudienceService } from '@open-event/admin'
import { Audience, AudienceSearchRequest, AudienceSearchResponse } from '@open-event/core'
import { MatDialog } from '@angular/material/dialog'
import { debounceTime, distinctUntilChanged } from 'rxjs'
import { AudienceChangeDialogComponent } from './audience-change-dialog/audience-change-dialog.component'
import { AudienceDeleteDialogComponent } from './audience-delete-dialog/audience-delete-dialog.component'
import { MatFormFieldModule } from '@angular/material/form-field'
import { MatButtonModule } from '@angular/material/button'
import { MatInputModule } from '@angular/material/input'
import { HotToastService } from '@ngxpert/hot-toast'
import { PageEvent } from '@angular/material/paginator'
import { AudienceTableComponent } from './audience-table/audience-table.component'
import { BoardComponent, BoardToolbarActions } from '../../shared/board/board.component'

@Component({
  selector: 'admin-audience',
  imports: [MatCardModule, MatIconModule, MatToolbarModule, MatFormFieldModule, MatButtonModule, MatInputModule, TranslatePipe, AudienceTableComponent, BoardComponent, BoardToolbarActions],
  templateUrl: './audience.component.html',
  styleUrl: './audience.component.scss'
})
export class AudienceComponent implements OnInit {
  reloading: boolean = false
  pageSize: number = 20
  pageNumber: number = 0
  totalElements: number = 0
  data: Audience[] = []
  keyUp: EventEmitter<string> = new EventEmitter<string>()
  request = new AudienceSearchRequest('')
  private service = inject(AudienceService)
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
    this.search()
    this.keyUp.pipe(debounceTime(500), distinctUntilChanged()).subscribe((query) => (this.fullTextSearch = query))
  }

  search() {
    this.load(0, this.pageSize)
  }

  reload() {
    this.load(0, this.pageSize)
  }

  handlePageChange(event: PageEvent) {
    if (this.reloading) return
    this.pageSize = event.pageSize
    this.load(event.pageIndex, event.pageSize)
  }

  create() {
    const dialogRef = this.dialog.open(AudienceChangeDialogComponent, {
      width: '350px',
      data: null
    })
    dialogRef.afterClosed().subscribe(() => this.search())
  }

  edit(entry: Audience) {
    const dialogRef = this.dialog.open(AudienceChangeDialogComponent, {
      width: '350px',
      data: entry
    })
    dialogRef.afterClosed().subscribe(() => this.search())
  }

  delete(entry: Audience) {
    const dialogRef = this.dialog.open(AudienceDeleteDialogComponent, {
      width: '350px',
      data: entry
    })
    dialogRef.afterClosed().subscribe(() => this.search())
  }

  private load(page: number, size: number) {
    if (this.reloading) return
    this.reloading = true
    this.service.search(this.request, page, size).subscribe({
      next: (value) => this.handleData(value),
      error: (e) => this.handleError(e)
    })
  }

  private handleData(response: AudienceSearchResponse) {
    const p = response.result
    this.data = p.content
    this.pageSize = p.pageable.size
    this.pageNumber = p.pageable.number
    this.totalElements = p.totalSize
    this.reloading = false
  }

  private handleError(err: any) {
    if (err) this.toast.error()
    this.reloading = false
  }
}
