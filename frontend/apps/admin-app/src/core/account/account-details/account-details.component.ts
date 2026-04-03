import { Component, computed, resource, signal, inject } from '@angular/core'
import { ActivatedRoute } from '@angular/router'
import { AccountService } from '@open-event/admin'
import { toPromise } from '@open-event/shared'
import { MatCardModule } from '@angular/material/card'
import { MatToolbarModule } from '@angular/material/toolbar'
import { AccountDetailsTitleComponent } from '../account-details-title/account-details-title.component'
import { AccountDetailsPreferencesComponent } from '../account-details-preferences/account-details-preferences.component'
import { AccountDetailsProfileComponent } from '../account-details-profile/account-details-profile.component'
import { AccountDetailsAddressComponent } from '../account-details-address/account-details-address.component'
import { AccountDetailsEventsComponent } from '../account-details-events/account-details-events.component'
import { MatIconModule } from '@angular/material/icon'
import { MatButtonModule } from '@angular/material/button'
import { Location } from '@angular/common'
import { MatTabsModule } from '@angular/material/tabs'
import { BoardComponent } from '../../../shared/board/board.component'
import { TranslatePipe } from '@ngx-translate/core'

@Component({
  selector: 'admin-account-details',
  imports: [
    MatCardModule,
    MatToolbarModule,
    MatIconModule,
    MatButtonModule,
    AccountDetailsTitleComponent,
    AccountDetailsPreferencesComponent,
    AccountDetailsProfileComponent,
    AccountDetailsAddressComponent,
    AccountDetailsEventsComponent,
    MatTabsModule,
    BoardComponent,
    TranslatePipe
  ],
  templateUrl: './account-details.component.html',
  styleUrl: './account-details.component.scss'
})
export class AccountDetailsComponent {
  private service = inject(AccountService)
  private route = inject(ActivatedRoute)
  private location = inject(Location)

  id = signal(-1)

  private accountResource = resource({
    params: this.id,
    loader: (param) => toPromise(this.service.getAccount(param.params), param.abortSignal)
  })

  readonly account = computed(this.accountResource.value ?? undefined)
  readonly loading = this.accountResource.isLoading
  readonly error = this.accountResource.error

  constructor() {
    this.route.paramMap.subscribe((params) => {
      const id = params.get('id')!
      this.id.set(+id)
    })
  }

  back() {
    this.location.back()
  }
}
