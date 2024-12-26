import {Component, EventEmitter} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {AccountService} from "../model/account.service";
import {debounceTime, distinctUntilChanged} from "rxjs";
import {SearchService} from "../../search/model/search.service";
import {AccountSearchEntry, AccountSearchRequest, AccountSearchResponse} from "../../search/model/search-api";

@Component({
    selector: 'app-search-account-dialog',
    templateUrl: './search-account-dialog.component.html',
    styleUrls: ['./search-account-dialog.component.scss'],
    standalone: false
})
export class SearchAccountDialogComponent {

  reloading: boolean = false
  searching: boolean = false
  keyUp: EventEmitter<string> = new EventEmitter<string>()

  pageSize: number = 20
  pageIndex: number = 0
  totalSize: number = 0
  accounts: AccountSearchEntry[] = []

  displayedColumns: string[] = ['name', 'firstName', 'lastName', 'email']

  request: AccountSearchRequest = new AccountSearchRequest('')

  constructor(
    public dialogRef: MatDialogRef<SearchAccountDialogComponent>,
    private service: AccountService,
    private searchService: SearchService,
  ) {

  }

  ngOnInit() {
    this.keyUp.pipe(
      debounceTime(500),
      distinctUntilChanged()
    ).subscribe(query => this.search(query))
    this.reload()
  }

  search(query: string) {
    this.request.fullTextSearch = query
    if (this.searching) return
    this.reload()
  }

  reload() {
    if (this.reloading) return
    this.reloading = true

    this.searchService.searchAccounts(this.request, this.pageIndex, this.pageSize).subscribe({
      next: value => this.handleData(value),
      error: e => this.handleError(e)
    })
  }

  private handleData(response: AccountSearchResponse) {
    let result = response.result;
    this.accounts = result.content
    this.pageSize = result.pageable.size
    this.pageIndex = result.pageable.number
    this.totalSize = result.totalSize
    this.reloading = false
    this.searching = false
  }

  private handleError(e: any) {
    this.reloading = false
  }
}
