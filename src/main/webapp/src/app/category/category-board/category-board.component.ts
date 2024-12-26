import {Component, EventEmitter} from '@angular/core';
import {Category} from "../model/category-api";
import {debounceTime, distinctUntilChanged} from "rxjs";
import {SearchService} from "../../search/model/search.service";
import {CategorySearchRequest, CategorySearchResponse} from "../../search/model/search-api";
import {MatDialog} from "@angular/material/dialog";
import {CategoryService} from "../model/category.service";
import {CategoryDeleteDialogComponent} from "../category-delete-dialog/category-delete-dialog.component";
import {CategoryChangeDialogComponent} from "../category-change-dialog/category-change-dialog.component";

@Component({
    selector: 'app-category-board',
    templateUrl: './category-board.component.html',
    styleUrls: ['./category-board.component.scss'],
    standalone: false
})
export class CategoryBoardComponent {

  reloading: boolean = false
  pageSize: number = 20
  pageIndex: number = 0
  totalSize: number = 0
  categories: Category[] = []

  keyUp: EventEmitter<string> = new EventEmitter<string>()
  request = new CategorySearchRequest('')
  displayedColumns: string[] = ['name', 'cmd']

  constructor(private searchService: SearchService, private service: CategoryService, private dialog: MatDialog) {

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
    this.reload(0, this.pageSize)
  }

  private reload(page: number, size: number) {
    if (this.reloading) return
    this.reloading = true
    this.searchService.searchCategories(this.request, page, size).subscribe(
      {
        next: value => this.handleData(value),
        error: e => this.handleError(e)
      }
    )
  }


  private handleData(response: CategorySearchResponse) {
    let p = response.result
    this.categories = p.content
    this.pageSize = p.pageable.size
    this.pageIndex = p.pageable.number
    this.totalSize = p.totalSize
    this.reloading = false
  }

  private handleError(err: any) {
    // this.toast.error("Failed to load data")
    this.reloading = false
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
