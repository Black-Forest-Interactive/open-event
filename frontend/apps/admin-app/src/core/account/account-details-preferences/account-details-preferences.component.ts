import { Component, computed, inject, input, resource, ChangeDetectionStrategy } from '@angular/core'
import { Account } from '@open-event/core'
import { toPromise } from '@open-event/shared'
import { AccountService } from '@open-event/admin'
import { BoardCardComponent } from '../../../shared/board-card/board-card.component'

@Component({
  selector: 'admin-account-details-preferences',
  imports: [BoardCardComponent],
  templateUrl: './account-details-preferences.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './account-details-preferences.component.scss'
})
export class AccountDetailsPreferencesComponent {
  data = input.required<Account>()
  private service = inject(AccountService)
  private preferencesResource = resource({
    params: this.data,
    loader: (param) => toPromise(this.service.getPreferences(param.params.id), param.abortSignal)
  })

  readonly preferences = computed(this.preferencesResource.value ?? undefined)
  readonly loading = this.preferencesResource.isLoading
  readonly error = this.preferencesResource.error
}
