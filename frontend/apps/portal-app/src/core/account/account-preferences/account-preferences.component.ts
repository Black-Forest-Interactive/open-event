import { Component, inject, resource } from '@angular/core'
import { TranslatePipe } from '@ngx-translate/core'
import { MatCard } from '@angular/material/card'
import { MatButton } from '@angular/material/button'
import { MatIcon } from '@angular/material/icon'
import { MatDivider } from '@angular/material/divider'
import { LoadingBarComponent, toPromise } from '@open-event/shared'
import { AccountService } from '@open-event/portal'
import { PreferencesChangeRequest } from '@open-event/core'
import { HotToastService } from '@ngxpert/hot-toast'

@Component({
  selector: 'portal-account-preferences',
  imports: [TranslatePipe, MatCard, MatButton, MatIcon, MatDivider, LoadingBarComponent],
  templateUrl: './account-preferences.component.html',
  styleUrl: './account-preferences.component.scss'
})
export class AccountPreferencesComponent {
  private service = inject(AccountService)
  private toast = inject(HotToastService)

  private preferencesResource = resource({
    loader: (p) => toPromise(this.service.getPreferences(), p.abortSignal)
  })

  readonly preferences = this.preferencesResource.value
  readonly loading = this.preferencesResource.isLoading

  subscribe() { this.update(true) }
  unsubscribe() { this.update(false) }

  private update(enabled: boolean) {
    const p = this.preferences()
    const request = new PreferencesChangeRequest(
      { enabled },
      { enabled: p?.communicationPreferences?.enabled ?? false },
      { enabled: p?.notificationPreferences?.enabled ?? false }
    )
    this.service.updatePreferences(request).subscribe({
      next: (value) => this.preferencesResource.set(value),
      error: () => this.toast.error()
    })
  }
}
