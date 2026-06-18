import { computed, effect, inject, Injectable, resource } from '@angular/core'
import { Title } from '@angular/platform-browser'
import { SettingsService } from '@open-event/portal'
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
      title: 'nav.group.main',
      items: [
        new NavItem('/event', 'nav.item.discover', 'explore'),
        new NavItem('/event', 'nav.item.saved', 'bookmark', [], { view: 'saved' }),
        new NavItem('/event', 'nav.item.regs', 'event_available', [], { view: 'regs' }),
        new NavItem('/event', 'nav.item.own', 'campaign', [], { view: 'own' }),
        new NavItem('/account', 'account.type', 'person', [Roles.ACCOUNT_READ]),
        new NavItem('/address', 'address.title', 'contact_mail', [Roles.ADDRESS_READ])
      ]
    },
    {
      title: 'nav.group.communication',
      items: [new NavItem('/activity', 'activity.title', 'notifications', [Roles.ACTIVITY_READ]), new NavItem('/feedback', 'feedback.title', 'feedback', [Roles.FEEDBACK_WRITE])]
    },
    {
      title: 'nav.group.info',
      items: [new NavItem('/imprint', 'imprint.title', 'copyright')]
    }
  ]

  accessibleNavGroups = computed(() => this.navGroups.map((g) => ({ ...g, items: g.items.filter((item) => item.isAccessible(this.authService.getPrincipal())) })).filter((g) => g.items.length > 0))

  private titleResource = resource({
    loader: (param) => toPromise(this.settingsService.getTitle(), param.abortSignal)
  })

  title = computed(() => this.titleResource.value()?.text ?? '')

  constructor() {
    effect(() => this.pageTitle.setTitle(this.title()))
  }
}
