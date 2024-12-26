import {Component, EventEmitter} from '@angular/core';
import {AccountService} from "../../account/model/account.service";
import {AccountSearchEntry, AccountSearchRequest, AccountSearchResponse} from "../../search/model/search-api";
import {SearchService} from "../../search/model/search.service";
import {MatDialog} from "@angular/material/dialog";
import {debounceTime, distinctUntilChanged} from "rxjs";
import {AccountChangeDialogComponent} from "../../account/account-change-dialog/account-change-dialog.component";
import {Router} from "@angular/router";
import {HotToastService} from "@ngxpert/hot-toast";
import {EventCreateDialogComponent} from "../../event/event-create-dialog/event-create-dialog.component";
import {PageEvent} from "@angular/material/paginator";

@Component({
    selector: 'app-backoffice-account',
    templateUrl: './backoffice-account.component.html',
    styleUrl: './backoffice-account.component.scss',
    standalone: false
})
export class BackofficeAccountComponent {

  reloading: boolean = false
  pageSize: number = 20
  pageIndex: number = 0
  totalSize: number = 0
  accounts: AccountSearchEntry[] = []

  keyUp: EventEmitter<string> = new EventEmitter<string>()
  request = new AccountSearchRequest('')
  displayedColumns: string[] = ['name', 'firstname', 'lastname', 'email', 'phone', 'mobile', 'action']

  constructor(private searchService: SearchService, private service: AccountService,
              private dialog: MatDialog, private router: Router, private toast: HotToastService) {

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
    this.searchService.searchAccounts(this.request, page, size).subscribe(
      {
        next: value => this.handleData(value),
        error: e => this.handleError(e)
      }
    )
  }


  private handleData(response: AccountSearchResponse) {
    let p = response.result
    this.accounts = p.content
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
    const dialogRef = this.dialog.open(AccountChangeDialogComponent, {
      width: '350px',
      data: null
    })
    dialogRef.afterClosed().subscribe(d => this.search())
  }

  edit(entry: AccountSearchEntry) {
    const dialogRef = this.dialog.open(AccountChangeDialogComponent, {
      width: '350px',
      data: entry
    })
    dialogRef.afterClosed().subscribe(d => this.search())
  }

  delete(entry: AccountSearchEntry) {
    // const dialogRef = this.dialog.open(CategoryDeleteDialogComponent, {
    //   width: '350px',
    //   data: entry
    // })
    // dialogRef.afterClosed().subscribe(d => this.search())
  }

  createEvent(entry: AccountSearchEntry) {
    const dialogRef = this.dialog.open(EventCreateDialogComponent, {
      width: '1000px',
      data: entry.id,
    })
    dialogRef.afterClosed().subscribe()
  }

  handlePageEvent(event: PageEvent) {
    this.reload(event.pageIndex, event.pageSize)
  }
}
