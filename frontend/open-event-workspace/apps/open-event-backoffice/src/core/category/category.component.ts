import {Component, EventEmitter} from '@angular/core';
import {CommonModule} from '@angular/common';
import {MatCardModule} from "@angular/material/card";
import {MatIconModule} from "@angular/material/icon";
import {MatToolbarModule} from "@angular/material/toolbar";
import {TranslatePipe} from "@ngx-translate/core";
import {CategoryService} from "@open-event-workspace/backoffice";
import {Category, CategorySearchRequest, CategorySearchResponse} from "@open-event-workspace/core";
import {MatDialog} from "@angular/material/dialog";
import {debounceTime, distinctUntilChanged} from "rxjs";
import {CategoryChangeDialogComponent} from "./category-change-dialog/category-change-dialog.component";
import {CategoryDeleteDialogComponent} from "./category-delete-dialog/category-delete-dialog.component";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatButtonModule} from "@angular/material/button";
import {MatInputModule} from "@angular/material/input";
import {HotToastService} from "@ngxpert/hot-toast";
import {PageEvent} from "@angular/material/paginator";
import {CategoryTableComponent} from "./category-table/category-table.component";
import {BoardComponent, BoardToolbarActions} from "../../shared/board/board.component";

@Component({
  selector: 'boffice-category',
  imports: [CommonModule, MatCardModule, MatIconModule, MatToolbarModule, MatFormFieldModule, MatButtonModule, MatInputModule, TranslatePipe, CategoryTableComponent, BoardComponent, BoardToolbarActions],
  templateUrl: './category.component.html',
  styleUrl: './category.component.scss',
})
export class CategoryComponent {

  reloading: boolean = false
  pageSize: number = 20
  pageNumber: number = 0
  totalElements: number = 0
  data: Category[] = []

  keyUp: EventEmitter<string> = new EventEmitter<string>()
  request = new CategorySearchRequest('')

  constructor(private service: CategoryService, private toast: HotToastService, private dialog: MatDialog) {
  }


  ngOnInit() {
    this.search()
    this.keyUp.pipe(
      debounceTime(500),
      distinctUntilChanged()
    ).subscribe(query => this.fullTextSearch = query)
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
    this.service.search(this.request, page, size).subscribe(
      {
        next: value => this.handleData(value),
        error: e => this.handleError(e)
      }
    )
  }


  private handleData(response: CategorySearchResponse) {
    let p = response.result
    this.data = p.content
    this.pageSize = p.pageable.size
    this.pageNumber = p.pageable.number
    this.totalElements = p.totalSize
    this.reloading = false
  }

  private handleError(err: any) {
    if (err) this.toast.error(err)
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
    dialogRef.afterClosed().subscribe(d => this.search())
  }

  edit(entry: Category) {
    const dialogRef = this.dialog.open(CategoryChangeDialogComponent, {
      width: '350px',
      data: entry
    })
    dialogRef.afterClosed().subscribe(d => this.search())
  }

  delete(entry: Category) {
    const dialogRef = this.dialog.open(CategoryDeleteDialogComponent, {
      width: '350px',
      data: entry
    })
    dialogRef.afterClosed().subscribe(d => this.search())
  }

}
