import { Component, inject } from '@angular/core'
import { AppLayoutComponent, NavGroup } from '@open-event/ui'
import { AuthService, MainNavItem } from '@open-event/shared'
import { TranslatePipe } from '@ngx-translate/core'
import { MatMenu, MatMenuItem, MatMenuTrigger } from '@angular/material/menu'
import { MatIcon } from '@angular/material/icon'
import { MatDivider } from '@angular/material/divider'
import { RouterLink, RouterLinkActive } from '@angular/router'
import { ActivityButtonComponent } from '../../core/activity/activity-button/activity-button.component'
import { AppService } from '../app.service'
import { DashboardService } from './dashboard.service'
import { Roles } from '../roles'

@Component({
  selector: 'portal-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
  imports: [AppLayoutComponent, TranslatePipe, MatMenu, MatMenuItem, MatMenuTrigger, MatIcon, MatDivider, RouterLink, RouterLinkActive, ActivityButtonComponent],
  standalone: true
})
export class DashboardComponent {
  protected readonly appService = inject(AppService)
  protected readonly dashboardService = inject(DashboardService)
  private readonly authService = inject(AuthService)

  readonly navGroups: NavGroup[] = [
    {
      title: 'nav.group.main',
      items: [
        new MainNavItem('/event', 'event.type', 'event_note'),
        new MainNavItem('/account', 'account.type', 'person', [Roles.ACCOUNT_READ]),
        new MainNavItem('/address', 'address.title', 'contact_mail', [Roles.ADDRESS_READ])
      ]
    },
    {
      title: 'nav.group.communication',
      items: [
        new MainNavItem('/activity', 'activity.title', 'notifications', [Roles.ACTIVITY_READ]),
        new MainNavItem('/feedback', 'feedback.title', 'feedback', [Roles.FEEDBACK_WRITE])
      ]
    },
    {
      title: 'nav.group.info',
      items: [
        new MainNavItem('/imprint', 'imprint.title', 'copyright')
      ]
    }
  ]
    .map((g) => ({ ...g, items: g.items.filter((item) => item.isAccessible(this.authService)) }))
    .filter((g) => g.items.length > 0)
}
