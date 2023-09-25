import {Component, EventEmitter} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {AccountService} from "../model/account.service";
import {debounceTime, distinctUntilChanged} from "rxjs";
import {Account} from "../model/account-api";
import {Page} from "../../shared/model/page";

@Component({
  selector: 'app-search-account-dialog',
  templateUrl: './search-account-dialog.component.html',
  styleUrls: ['./search-account-dialog.component.scss']
})
export class SearchAccountDialogComponent {

  reloading: boolean = false
  searching: boolean = false
  keyUp: EventEmitter<string> = new EventEmitter<string>()

  pageSize: number = 20
  pageIndex: number = 0
  totalSize: number = 0
  accounts: Account[] = []
  query: string = ''

  displayedColumns: string[] = ['name', 'firstName', 'lastName', 'email'];

  constructor(
    public dialogRef: MatDialogRef<SearchAccountDialogComponent>,
    private service: AccountService
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
    this.query = query
    if (this.searching) return
    this.reload()
  }

  reload() {
    if (this.reloading) return
    this.reloading = true
    if (this.query.length <= 0) {
      this.service.getAllAccounts(this.pageIndex, this.pageSize).subscribe({
        next: value => this.handleData(value),
        error: e => this.handleError(e)
      })
    } else {
      this.service.searchAccounts(this.query, this.pageIndex, this.pageSize).subscribe({
        next: value => this.handleData(value),
        error: e => this.handleError(e)
      })
    }
  }

  private handleData(value: Page<Account>) {
    this.accounts = value.content
    this.pageSize = value.pageable.size
    this.pageIndex = value.pageable.number
    this.totalSize = value.totalSize
    this.reloading = false
    this.searching = false
  }

  private handleError(e: any) {
    this.reloading = false
  }
}
