import {Component, EventEmitter} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AccountSearchEntry, AccountSearchRequest, AccountSearchResponse, AccountService, SearchService} from "@open-event-workspace/core";
import {MatDialogActions, MatDialogClose, MatDialogContent, MatDialogRef, MatDialogTitle} from "@angular/material/dialog";
import {debounceTime, distinctUntilChanged} from "rxjs";
import {TranslatePipe} from "@ngx-translate/core";
import {MatFormField} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatIcon} from "@angular/material/icon";
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {MatButton, MatIconButton, MatMiniFabButton} from "@angular/material/button";
import {MatTableModule} from "@angular/material/table";
import {LoadingBarComponent} from "@open-event-workspace/shared";

@Component({
  selector: 'app-search-account-dialog',
  imports: [CommonModule, MatDialogTitle, MatDialogContent, TranslatePipe, MatFormField, MatInput, MatIcon, MatProgressSpinner, MatIconButton, MatMiniFabButton, MatTableModule, MatDialogActions, MatButton, MatDialogClose, LoadingBarComponent],
  templateUrl: './search-account-dialog.component.html',
  styleUrl: './search-account-dialog.component.scss',
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
