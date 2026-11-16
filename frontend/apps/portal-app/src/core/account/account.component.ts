import { Component, DestroyRef, inject, ChangeDetectionStrategy } from '@angular/core'

import { AccountProfileComponent } from './account-profile/account-profile.component'
import { AccountPreferencesComponent } from './account-preferences/account-preferences.component'
import { TourService } from '@open-event/shared'
import { accountTour } from './account.tour'

@Component({
  selector: 'portal-account',
  imports: [AccountProfileComponent, AccountPreferencesComponent],
  templateUrl: './account.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './account.component.scss'
})
export class AccountComponent {
  private tourService = inject(TourService)
  private destroyRef = inject(DestroyRef)

  constructor() {
    this.tourService.register(accountTour)
    this.destroyRef.onDestroy(() => this.tourService.unregister(accountTour.id))
  }
}
