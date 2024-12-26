import {Component, EventEmitter, Input} from '@angular/core';
import {Account, AccountChangeRequest} from "../model/account-api";
import {MatDialog} from "@angular/material/dialog";
import {AccountService} from "../model/account.service";
import {debounceTime, distinctUntilChanged} from "rxjs";
import {CreateAccountDialogComponent} from "../create-account-dialog/create-account-dialog.component";
import {HotToastService} from "@ngxpert/hot-toast";
import {TranslateService} from "@ngx-translate/core";
import {FormControl, FormGroup} from "@angular/forms";
import {MatOptionSelectionChange} from "@angular/material/core";
import {AccountSearchEntry, AccountSearchRequest, AccountSearchResponse} from "../../search/model/search-api";
import {SearchService} from "../../search/model/search.service";

@Component({
    selector: 'app-select-account',
    templateUrl: './select-account.component.html',
    styleUrls: ['./select-account.component.scss'],
    standalone: false
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
  @Input() form: FormGroup | undefined

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

    if (this.form) this.form.setValue({owner: account.id})
    this.partialText = account.name
    console.log(this.form?.value)
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
