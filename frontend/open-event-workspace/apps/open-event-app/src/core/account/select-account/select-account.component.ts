import {Component, EventEmitter, input} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Account, AccountChangeRequest, AccountSearchEntry, AccountSearchRequest, AccountSearchResponse, AccountService, SearchService} from "@open-event-workspace/core";
import {FormControl, FormGroup} from "@angular/forms";
import {MatDialog} from "@angular/material/dialog";
import {debounceTime, distinctUntilChanged} from "rxjs";
import {TranslatePipe, TranslateService} from "@ngx-translate/core";
import {HotToastService} from "@ngxpert/hot-toast";
import {CreateAccountDialogComponent} from "../create-account-dialog/create-account-dialog.component";
import {MatOption, MatOptionSelectionChange} from "@angular/material/core";
import {MatFormField} from "@angular/material/form-field";
import {MatAutocomplete, MatAutocompleteTrigger} from "@angular/material/autocomplete";
import {MatInput} from "@angular/material/input";
import {MatIcon} from "@angular/material/icon";
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-select-account',
  imports: [CommonModule, MatFormField, TranslatePipe, MatAutocompleteTrigger, MatInput, MatAutocomplete, MatOption, MatIcon, MatProgressSpinner, MatButton],
  templateUrl: './select-account.component.html',
  styleUrl: './select-account.component.scss',
})
export class SelectAccountComponent {
  reloading: boolean = false
  searching: boolean = false
  keyUp: EventEmitter<string> = new EventEmitter<string>()

  pageSize: number = 20
  pageIndex: number = 0
  totalSize: number = 0
  accounts: AccountSearchEntry[] = []
  request: AccountSearchRequest = new AccountSearchRequest('')

  accountCreating: boolean = false
  partialText = '';

  selectCtrl = new FormControl('')

  form = input<FormGroup>()

  constructor(
    private service: AccountService,
    private searchService: SearchService,
    private dialog: MatDialog,
    private toastService: HotToastService,
    private translate: TranslateService
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
      }
    )
  }

  showCreateAccountDialog() {
    let dialogRef = this.dialog.open(CreateAccountDialogComponent)
    dialogRef.afterClosed().subscribe(request => {
      if (request) this.createAccount(request)
    })
  }

  select(event: MatOptionSelectionChange<string>, account: AccountSearchEntry) {
    if (!event.source.selected) return

    let f = this.form()
    if (f) f.setValue({owner: account.id})
    this.partialText = account.name
  }

  private handleData(response: AccountSearchResponse) {
    let result = response.result
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

  private createAccount(request: AccountChangeRequest) {
    if (this.accountCreating) return
    this.accountCreating = true

    this.service.createAccount(request).subscribe(a => this.handleAccountCreationResult(a))
  }

  private handleAccountCreationResult(a: Account) {
    this.translate.get('account.message.created').subscribe(msg => this.toastService.success(msg))
    this.accountCreating = false
    this.selectCtrl.setValue('')
    this.reload()
  }
}
