import { Component, inject, OnInit, signal } from '@angular/core'

import { TranslatePipe } from '@ngx-translate/core'
import { MatCardModule } from '@angular/material/card'
import { MatButtonModule } from '@angular/material/button'
import { MatIconModule } from '@angular/material/icon'
import { MatDivider } from '@angular/material/divider'
import { LoadingBarComponent } from '@open-event/shared'
import { AccountService } from '@open-event/portal'
import { Preferences, PreferencesChangeRequest } from '@open-event/core'
import { HotToastService } from '@ngxpert/hot-toast'

@Component({
  selector: 'portal-account-preferences',
  imports: [TranslatePipe, MatCardModule, MatButtonModule, MatIconModule, MatDivider, LoadingBarComponent],
  templateUrl: './account-preferences.component.html',
  styleUrl: './account-preferences.component.scss'
})
export class AccountPreferencesComponent implements OnInit {
  private service = inject(AccountService)
  private toast = inject(HotToastService)

  preferences = signal<Preferences | undefined>(undefined)
  reloading = signal(false)

  ngOnInit() {
    this.reload()
  }

  private reload() {
    if (this.reloading()) return
    this.reloading.set(true)
    this.service.getPreferences().subscribe({
      next: (value) => this.handleData(value),
      error: (err) => this.handleError(err),
      complete: () => this.reloading.set(false)
    })
  }

  private handleData(value: Preferences) {
    this.preferences.set(value)
    this.reloading.set(false)
  }

  private handleError(err: any) {
    if (err) this.toast.error()
    this.reloading.set(false)
  }

  subscribe() {
    this.update(true)
  }

  unsubscribe() {
    this.update(false)
  }

  private update(enabled: boolean) {
    if (this.reloading()) return
    this.reloading.set(true)
    const request = new PreferencesChangeRequest(
      { enabled: enabled },
      {
        enabled: this.preferences()?.communicationPreferences?.enabled ?? false
      },
      {
        enabled: this.preferences()?.notificationPreferences?.enabled ?? false
      }
    )
    this.service.updatePreferences(request).subscribe({
      next: (value) => this.handleData(value),
      error: (err) => this.handleError(err),
      complete: () => this.reloading.set(false)
    })
  }
}
