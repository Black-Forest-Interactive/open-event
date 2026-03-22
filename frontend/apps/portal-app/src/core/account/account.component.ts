import { Component } from '@angular/core'

import { AccountProfileComponent } from './account-profile/account-profile.component'
import { AccountPreferencesComponent } from './account-preferences/account-preferences.component'

@Component({
  selector: 'app-account',
  imports: [AccountProfileComponent, AccountPreferencesComponent],
  templateUrl: './account.component.html',
  styleUrl: './account.component.scss'
})
export class AccountComponent {}
