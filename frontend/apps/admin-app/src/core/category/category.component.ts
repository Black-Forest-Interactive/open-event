import { Component, EventEmitter, inject, OnInit } from '@angular/core'

import { MatCardModule } from '@angular/material/card'
import { MatIconModule } from '@angular/material/icon'
import { MatToolbarModule } from '@angular/material/toolbar'
import { TranslatePipe } from '@ngx-translate/core'
import { CategoryService } from '@open-event/admin'
import { Category, CategorySearchRequest, CategorySearchResponse } from '@open-event/core'
import { MatDialog } from '@angular/material/dialog'
import { debounceTime, distinctUntilChanged } from 'rxjs'
import { CategoryChangeDialogComponent } from './category-change-dialog/category-change-dialog.component'
import { CategoryDeleteDialogComponent } from './category-delete-dialog/category-delete-dialog.component'
import { MatFormFieldModule } from '@angular/material/form-field'
import { MatButtonModule } from '@angular/material/button'
import { MatInputModule } from '@angular/material/input'
import { HotToastService } from '@ngxpert/hot-toast'
import { PageEvent } from '@angular/material/paginator'
import { CategoryTableComponent } from './category-table/category-table.component'
import { BoardComponent, BoardToolbarActions } from '../../shared/board/board.component'

@Component({
  selector: 'admin-category',
  imports: [MatCardModule, MatIconModule, MatToolbarModule, MatFormFieldModule, MatButtonModule, MatInputModule, TranslatePipe, CategoryTableComponent, BoardComponent, BoardToolbarActions],
  templateUrl: './category.component.html',
  styleUrl: './category.component.scss'
})
export class CategoryComponent implements OnInit {
  private service = inject(CategoryService)
  private toast = inject(HotToastService)
  private dialog = inject(MatDialog)

  reloading: boolean = false
  pageSize: number = 20
  pageNumber: number = 0
  totalElements: number = 0
  data: Category[] = []

  keyUp: EventEmitter<string> = new EventEmitter<string>()
  request = new CategorySearchRequest('')

  ngOnInit() {
    this.search()
    this.keyUp.pipe(debounceTime(500), distinctUntilChanged()).subscribe((query) => (this.fullTextSearch = query))
  }

  set fullTextSearch(val: string) {
    if (this.request.fullTextSearch === val) return
    this.request.fullTextSearch = val
    this.search()
  }

  get fullTextSearch(): string {
    return this.request.fullTextSearch
  }

  search() {
    this.load(0, this.pageSize)
  }

  reload() {
    this.load(0, this.pageSize)
  }

  private load(page: number, size: number) {
    if (this.reloading) return
    this.reloading = true
    this.service.search(this.request, page, size).subscribe({
      next: (value) => this.handleData(value),
      error: (e) => this.handleError(e)
    })
  }

  private handleData(response: CategorySearchResponse) {
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

  handlePageChange(event: PageEvent) {
    if (this.reloading) return
    this.pageSize = event.pageSize
    this.load(event.pageIndex, event.pageSize)
  }

  create() {
    const dialogRef = this.dialog.open(CategoryChangeDialogComponent, {
      width: '350px',
      data: null
    })
    dialogRef.afterClosed().subscribe(() => this.search())
  }

  edit(entry: Category) {
    const dialogRef = this.dialog.open(CategoryChangeDialogComponent, {
      width: '350px',
      data: entry
    })
    dialogRef.afterClosed().subscribe(() => this.search())
  }

  delete(entry: Category) {
    const dialogRef = this.dialog.open(CategoryDeleteDialogComponent, {
      width: '350px',
      data: entry
    })
    dialogRef.afterClosed().subscribe(() => this.search())
  }
}
