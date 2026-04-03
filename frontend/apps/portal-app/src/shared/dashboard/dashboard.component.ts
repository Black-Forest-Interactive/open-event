import { AfterViewInit, ChangeDetectorRef, Component, inject, OnInit, ViewChild } from '@angular/core'
import { NavigationEnd, Router, RouterLink, RouterOutlet } from '@angular/router'
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout'
import { filter, map, Observable, withLatestFrom } from 'rxjs'
import { MatSidenav, MatSidenavContainer, MatSidenavContent } from '@angular/material/sidenav'
import { AuthService, MainMenuComponent, MainNavItem } from '@open-event/shared'
import { DashboardService } from './dashboard.service'
import { AsyncPipe } from '@angular/common'
import { DashboardToolbarComponent } from '../dashboard-toolbar/dashboard-toolbar.component'
import { Roles } from '../roles'
import { AppService } from '../app.service'

@Component({
  selector: 'portal-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
  imports: [MatSidenavContainer, AsyncPipe, MatSidenav, MatSidenavContent, RouterLink, MainMenuComponent, RouterOutlet, DashboardToolbarComponent],
  standalone: true
})
export class DashboardComponent implements AfterViewInit, OnInit {
  authService = inject(AuthService)
  appService = inject(AppService)
  private breakpointObserver = inject(BreakpointObserver)
  private changeDetectorRef = inject(ChangeDetectorRef)
  service = inject(DashboardService)

  isHandset$: Observable<boolean>

  @ViewChild('drawer') drawer: MatSidenav | undefined

  navItems: MainNavItem[] = [
    new MainNavItem('/event', 'event.type', 'event_note'),
    new MainNavItem('/account', 'account.type', 'person', [Roles.ACCOUNT_READ]),
    new MainNavItem('/address', 'address.title', 'contact_mail', [Roles.ADDRESS_READ]),
    new MainNavItem('/activity', 'activity.title', 'notifications', [Roles.ACTIVITY_READ]),
    new MainNavItem('/feedback', 'feedback.title', 'feedback', [Roles.FEEDBACK_WRITE]),
    new MainNavItem('/imprint', 'imprint.title', 'copyright', [])
  ]

  accessibleItems: MainNavItem[] = []

  constructor() {
    const router = inject(Router)

    this.isHandset$ = this.breakpointObserver.observe(Breakpoints.Handset).pipe(map((result) => result.matches))

    router.events
      .pipe(
        withLatestFrom(this.isHandset$),
        filter(([a, b]) => b && a instanceof NavigationEnd)
      )
      .subscribe((_) => this.drawer?.close())
  }

  ngOnInit() {
    this.accessibleItems = this.navItems.filter((item) => item.isAccessible(this.authService))
  }

  ngAfterViewInit() {
    this.changeDetectorRef.detectChanges()
  }
}
