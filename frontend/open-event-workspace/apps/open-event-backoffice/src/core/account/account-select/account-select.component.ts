import {Component, computed, EventEmitter, output, resource, signal} from '@angular/core';
import {AccountSearchEntry, AccountSearchRequest} from "@open-event-workspace/core";
import {AccountService} from "@open-event-workspace/backoffice";
import {toPromise} from "@open-event-workspace/shared";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatAutocomplete, MatAutocompleteTrigger} from "@angular/material/autocomplete";
import {MatOption, MatOptionSelectionChange} from "@angular/material/core";
import {ReactiveFormsModule} from '@angular/forms';
import {debounceTime, distinctUntilChanged, filter} from "rxjs";
import {takeUntilDestroyed} from "@angular/core/rxjs-interop";
import {TranslatePipe} from "@ngx-translate/core";

@Component({
  selector: 'app-account-select',
  imports: [
    MatFormField,
    MatInput,
    MatAutocomplete,
    MatOption,
    MatLabel,
    ReactiveFormsModule,
    MatAutocompleteTrigger,
    TranslatePipe
  ],
  templateUrl: './account-select.component.html',
  styleUrl: './account-select.component.scss'
})
export class AccountSelectComponent {
  selectionChanged = output<AccountSearchEntry>()

  request = signal<AccountSearchRequest>(new AccountSearchRequest(''))

  accountResource = resource({
    request: this.request,
    loader: (param) => {
      return toPromise(this.service.search(param.request, 0, 10))
    }
  })
  private result = computed(() => this.accountResource.value()?.result ?? undefined)

  accounts = computed(() => this.result()?.content ?? [])
  totalSize = computed(() => this.result()?.totalSize ?? 0)
  loading = this.accountResource.isLoading
  error = this.accountResource.error

  keyUp: EventEmitter<string> = new EventEmitter<string>()


  constructor(private service: AccountService) {
    this.keyUp.pipe(
      debounceTime(500),
      filter(value => value.length > 3),
      distinctUntilChanged(),
      takeUntilDestroyed()
    ).subscribe(query => this.request.set(new AccountSearchRequest(query)))
  }

  select(event: MatOptionSelectionChange<string>, account: AccountSearchEntry) {
    if (!event.source.selected) return
    this.selectionChanged.emit(account)
  }
}
