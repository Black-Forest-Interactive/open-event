import { computed, effect, inject, Injectable, resource } from '@angular/core'
import { Title } from '@angular/platform-browser'
import { SettingsService } from '@open-event/admin'
import { AuthService, toPromise } from '@open-event/shared'
import { NavGroup, NavItem } from '@open-event/ui'
import { Roles } from '../roles'

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  private authService = inject(AuthService)
  private settingsService = inject(SettingsService)
  private pageTitle = inject(Title)

  private navGroups: NavGroup[] = [
    {
      title: 'nav.group.content',
      items: [
        new NavItem('/event', 'event.type', 'event_note', [Roles.EVENT_ADMIN]),
        new NavItem('/account', 'account.admin', 'manage_accounts', [Roles.ACCOUNT_ADMIN]),
        new NavItem('/category', 'category.type', 'label', [Roles.CATEGORY_ADMIN]),
        new NavItem('/audience', 'audience.type', 'groups', [Roles.AUDIENCE_ADMIN])
      ]
    },
    {
      title: 'nav.group.communication',
      items: [
        new NavItem('/activity', 'activity.title', 'notifications', [Roles.ACTIVITY_ADMIN]),
        new NavItem('/mail', 'mail.type', 'email', [Roles.MAIL_ADMIN]),
        new NavItem('/feedback', 'feedback.type', 'feedback', [Roles.FEEDBACK_ADMIN])
      ]
    },
    {
      title: 'nav.group.system',
      items: [
        new NavItem('/settings', 'settings.type', 'settings_applications', [Roles.SETTINGS_ADMIN]),
        new NavItem('/cache', 'cache.type', 'memory', [Roles.CACHE_ADMIN]),
        new NavItem('/search', 'search.type', 'manage_search', [Roles.SEARCH_ADMIN]),
        new NavItem('/backoffice', 'backoffice.type', 'admin_panel_settings', [Roles.BACKOFFICE_ACCESS])
      ]
    },
    {
      title: 'nav.group.monitoring',
      items: [new NavItem('/history', 'history.type', 'history', [Roles.HISTORY_ADMIN]), new NavItem('/issue', 'issue.type', 'bug_report', [Roles.ISSUE_ADMIN])]
    }
  ]

  accessibleNavGroups = computed(() => this.navGroups.map((g) => ({ ...g, items: g.items.filter((item) => item.isAccessible(this.authService.getPrincipal())) })).filter((g) => g.items.length > 0))

  private titleResource = resource({
    loader: (param) => toPromise(this.settingsService.getTitle(), param.abortSignal)
  })

  title = computed(() => this.titleResource.value()?.text ?? 'Backoffice')

  constructor() {
    effect(() => this.pageTitle.setTitle(this.title()))
  }
}
