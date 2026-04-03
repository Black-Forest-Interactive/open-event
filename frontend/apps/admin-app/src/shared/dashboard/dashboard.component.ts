import { Component, inject } from '@angular/core'
import { AppLayoutComponent, NavGroup } from '@open-event/ui'
import { AuthService } from '@open-event/shared'
import { AccountDisplayNamePipe } from '@open-event/core'
import { TranslatePipe } from '@ngx-translate/core'
import { MatMenu, MatMenuItem, MatMenuTrigger } from '@angular/material/menu'
import { MatIcon } from '@angular/material/icon'
import { MatDivider } from '@angular/material/divider'
import { RouterLink } from '@angular/router'
import { GravatarModule } from 'ngx-gravatar'
import { AppService } from '../app.service'
import { DashboardService } from './dashboard.service'
import { MainNavItem } from './main-nav-item'
import { Roles } from '../roles'

@Component({
  selector: 'admin-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
  imports: [AppLayoutComponent, TranslatePipe, AccountDisplayNamePipe, GravatarModule, MatMenu, MatMenuItem, MatMenuTrigger, MatIcon, MatDivider, RouterLink],
  standalone: true
})
export class DashboardComponent {
  protected readonly appService = inject(AppService)
  protected readonly dashboardService = inject(DashboardService)
  private readonly authService = inject(AuthService)

  readonly navGroups: NavGroup[] = [
    {
      title: 'nav.group.content',
      items: [
        new MainNavItem('/event', 'event.type', 'event_note', [Roles.EVENT_ADMIN]),
        new MainNavItem('/account', 'account.admin', 'manage_accounts', [Roles.ACCOUNT_ADMIN]),
        new MainNavItem('/category', 'category.type', 'label', [Roles.CATEGORY_ADMIN])
      ]
    },
    {
      title: 'nav.group.communication',
      items: [
        new MainNavItem('/activity', 'activity.title', 'notifications', [Roles.ACTIVITY_ADMIN]),
        new MainNavItem('/mail', 'mail.type', 'email', [Roles.MAIL_ADMIN]),
        new MainNavItem('/feedback', 'feedback.type', 'feedback', [Roles.FEEDBACK_ADMIN])
      ]
    },
    {
      title: 'nav.group.system',
      items: [
        new MainNavItem('/settings', 'settings.type', 'settings_applications', [Roles.SETTINGS_ADMIN]),
        new MainNavItem('/cache', 'cache.type', 'memory', [Roles.CACHE_ADMIN]),
        new MainNavItem('/search', 'search.type', 'manage_search', [Roles.SEARCH_ADMIN]),
        new MainNavItem('/backoffice', 'backoffice.type', 'admin_panel_settings', [Roles.BACKOFFICE_ACCESS])
      ]
    },
    {
      title: 'nav.group.monitoring',
      items: [
        new MainNavItem('/history', 'history.type', 'history', [Roles.HISTORY_ADMIN]),
        new MainNavItem('/issue', 'issue.type', 'bug_report', [Roles.ISSUE_ADMIN])
      ]
    }
  ]
    .map((g) => ({ ...g, items: g.items.filter((item) => item.isAccessible(this.authService)) }))
    .filter((g) => g.items.length > 0)
}
