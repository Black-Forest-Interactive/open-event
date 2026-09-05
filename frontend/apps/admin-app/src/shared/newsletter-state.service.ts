import { computed, inject, Injectable, resource } from '@angular/core'
import { TranslateService } from '@ngx-translate/core'
import { HotToastService } from '@ngxpert/hot-toast'
import { AuthService, toPromise } from '@open-event/shared'
import { NotificationService } from '@open-event/admin'
import { Roles } from './roles'

@Injectable({ providedIn: 'root' })
export class NewsletterStateService {
  private notificationService = inject(NotificationService)
  private toast = inject(HotToastService)
  private translateService = inject(TranslateService)
  private authService = inject(AuthService)

  readonly accessible = computed(() => this.authService.hasRole(Roles.MAIL_ADMIN, Roles.MAIL_READ))

  private settingParams = computed(() => (this.accessible() ? true : undefined))

  private settingResource = resource({
    params: this.settingParams,
    loader: (p) => toPromise(this.notificationService.getNewsletterSetting(), p.abortSignal)
  })

  readonly enabled = computed(() => this.settingResource.value()?.enabled ?? true)
  readonly loaded = computed(() => this.settingResource.value() != null)

  toggle() {
    const setting = this.settingResource.value()
    if (!setting) return
    const enabled = !setting.enabled
    this.notificationService.setNewsletterEnabled(setting.id, enabled).subscribe({
      next: (updated) => {
        this.settingResource.set(updated)
        const key = enabled ? 'newsletter.message.enabled' : 'newsletter.message.disabled'
        this.translateService.get(key).subscribe((t) => this.toast.success(t))
      }
    })
  }
}
