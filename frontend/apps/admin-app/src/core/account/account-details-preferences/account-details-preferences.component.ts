import { Component, computed, input, resource, inject } from '@angular/core'
import { Account } from '@open-event/core'
import { toPromise } from '@open-event/shared'
import { AccountService } from '@open-event/admin'
import { BoardCardComponent } from '../../../shared/board-card/board-card.component'

@Component({
  selector: 'admin-account-details-preferences',
  imports: [BoardCardComponent],
  templateUrl: './account-details-preferences.component.html',
  styleUrl: './account-details-preferences.component.scss'
})
export class AccountDetailsPreferencesComponent {
  private service = inject(AccountService)

  data = input.required<Account>()

  private preferencesResource = resource({
    params: this.data,
    loader: (param) => toPromise(this.service.getPreferences(param.params.id), param.abortSignal)
  })

  readonly preferences = computed(this.preferencesResource.value ?? undefined)
  readonly loading = this.preferencesResource.isLoading
  readonly error = this.preferencesResource.error
}
