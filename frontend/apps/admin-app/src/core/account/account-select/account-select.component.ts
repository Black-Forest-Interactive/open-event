import { Component, computed, EventEmitter, inject, output, resource, signal } from '@angular/core'
import { AccountSearchEntry, AccountSearchRequest } from '@open-event/core'
import { AccountService } from '@open-event/admin'
import { toPromise } from '@open-event/shared'
import { MatFormField, MatLabel } from '@angular/material/form-field'
import { MatInput } from '@angular/material/input'
import { MatAutocomplete, MatAutocompleteTrigger } from '@angular/material/autocomplete'
import { MatOption, MatOptionSelectionChange } from '@angular/material/core'
import { ReactiveFormsModule } from '@angular/forms'
import { debounceTime, distinctUntilChanged, filter } from 'rxjs'
import { takeUntilDestroyed } from '@angular/core/rxjs-interop'
import { TranslatePipe } from '@ngx-translate/core'

@Component({
  selector: 'admin-account-select',
  imports: [MatFormField, MatInput, MatAutocomplete, MatOption, MatLabel, ReactiveFormsModule, MatAutocompleteTrigger, TranslatePipe],
  templateUrl: './account-select.component.html',
  styleUrl: './account-select.component.scss'
})
export class AccountSelectComponent {
  private service = inject(AccountService)

  selectionChanged = output<AccountSearchEntry>()

  request = signal<AccountSearchRequest>(new AccountSearchRequest(''))

  private accountResource = resource({
    params: this.request,
    loader: (param) => toPromise(this.service.search(param.params, 0, 10), param.abortSignal)
  })
  private result = computed(() => this.accountResource.value()?.result ?? undefined)

  readonly accounts = computed(() => this.result()?.content ?? [])
  readonly totalSize = computed(() => this.result()?.totalSize ?? 0)
  readonly loading = this.accountResource.isLoading
  readonly error = this.accountResource.error

  keyUp: EventEmitter<string> = new EventEmitter<string>()

  constructor() {
    this.keyUp
      .pipe(
        debounceTime(500),
        filter((value) => value.length > 3),
        distinctUntilChanged(),
        takeUntilDestroyed()
      )
      .subscribe((query) => this.request.set(new AccountSearchRequest(query)))
  }

  select(event: MatOptionSelectionChange<string>, account: AccountSearchEntry) {
    if (!event.source.selected) return
    this.selectionChanged.emit(account)
  }
}
