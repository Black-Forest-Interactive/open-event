import { Component, input } from '@angular/core'
import { AccountInfo } from '@open-event/core'
import { AccountDisplayNamePipe } from '@open-event/ui'
import { FALLBACK, GravatarModule, RATING } from 'ngx-gravatar'

@Component({
  selector: 'portal-account',
  imports: [AccountDisplayNamePipe, GravatarModule],
  templateUrl: './account.component.html',
  styleUrl: './account.component.scss'
})
export class AccountComponent {
  account = input.required<AccountInfo>()
  showUserName = input(true)

  protected readonly FALLBACK = FALLBACK
  protected readonly RATING = RATING
}
