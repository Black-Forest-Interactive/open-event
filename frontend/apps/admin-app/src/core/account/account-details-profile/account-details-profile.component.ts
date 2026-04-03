import { Component, computed, input, resource, inject } from '@angular/core'
import { Account } from '@open-event/core'
import { toPromise } from '@open-event/shared'
import { AccountService } from '@open-event/admin'
import { TranslatePipe } from '@ngx-translate/core'
import { BoardCardComponent } from '../../../shared/board-card/board-card.component'

@Component({
  selector: 'admin-account-details-profile',
  imports: [TranslatePipe, BoardCardComponent],
  templateUrl: './account-details-profile.component.html',
  styleUrl: './account-details-profile.component.scss'
})
export class AccountDetailsProfileComponent {
  private service = inject(AccountService)

  data = input.required<Account>()

  private profileResource = resource({
    params: this.data,
    loader: (param) => toPromise(this.service.getProfile(param.params.id), param.abortSignal)
  })

  readonly profile = computed(this.profileResource.value ?? undefined)
  readonly loading = this.profileResource.isLoading
  readonly error = this.profileResource.error
}
