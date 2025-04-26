import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';
import {MatButton} from "@angular/material/button";
import {MatCard} from "@angular/material/card";
import {MatIcon} from "@angular/material/icon";
import {TranslatePipe} from "@ngx-translate/core";
import {AccountSearchEntry, AccountSearchRequest, AccountSearchResponse} from "@open-event-workspace/core";
import {AccountService} from "@open-event-workspace/backoffice";
import {HotToastService} from "@ngxpert/hot-toast";
import {MatDialog} from "@angular/material/dialog";
import {PageEvent} from "@angular/material/paginator";
import {AccountChangeDialogComponent} from "./account-change-dialog/account-change-dialog.component";
import {AccountDeleteDialogComponent} from "./account-delete-dialog/account-delete-dialog.component";
import {AccountTableComponent} from "./account-table/account-table.component";
import {BoardComponent, BoardToolbarActions} from "../../shared/board/board.component";
import {EventCreateDialogComponent} from "../event/event-create-dialog/event-create-dialog.component";

@Component({
  selector: 'boffice-account',
  imports: [CommonModule, MatButton, MatCard, MatIcon, TranslatePipe, AccountTableComponent, BoardComponent, BoardToolbarActions],
  templateUrl: './account.component.html',
  styleUrl: './account.component.scss',
})
export class AccountComponent {

  reloading: boolean = false
  pageSize: number = 20
  pageNumber: number = 0
  totalElements: number = 0
  data: AccountSearchEntry[] = []

  request = new AccountSearchRequest('')

  constructor(private service: AccountService, private toast: HotToastService, private dialog: MatDialog) {
  }


  ngOnInit() {
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


  private handleData(response: AccountSearchResponse) {
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
    const dialogRef = this.dialog.open(AccountDeleteDialogComponent, {
      width: '350px',
      data: entry
    })
    dialogRef.afterClosed().subscribe(d => this.search())
  }

  createEvent(entry: AccountSearchEntry) {
    this.dialog.open(EventCreateDialogComponent, {data: entry}).afterClosed().subscribe(value => {
      if (value) this.search()
    })
  }
}
