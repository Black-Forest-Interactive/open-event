import {AfterViewInit, ChangeDetectorRef, Component, ViewChild} from '@angular/core';
import {NavigationEnd, Router, RouterLink, RouterOutlet} from "@angular/router";
import {BreakpointObserver, Breakpoints} from "@angular/cdk/layout";
import {filter, map, Observable, withLatestFrom} from "rxjs";
import {MatSidenav, MatSidenavContainer, MatSidenavContent} from "@angular/material/sidenav";
import {MainNavItem} from "./main-nav-item";
import {AuthService, MainMenuComponent} from "@open-event-workspace/shared";
import {DashboardService} from "./dashboard.service";
import {AsyncPipe} from "@angular/common";
import {DashboardToolbarComponent} from "../dashboard-toolbar/dashboard-toolbar.component";
import {Roles} from "../roles";

@Component({
  selector: 'boffice-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
  imports: [
    MatSidenavContainer,
    AsyncPipe,
    MatSidenav,
    MatSidenavContent,
    RouterLink,
    MainMenuComponent,
    RouterOutlet,
    DashboardToolbarComponent
  ],
  standalone: true
})
export class DashboardComponent implements AfterViewInit {


  isHandset$: Observable<boolean>


  @ViewChild('drawer') drawer: MatSidenav | undefined

  navItems: MainNavItem[] = [
    new MainNavItem('/event', 'event.type', 'event_note', [Roles.EVENT_ADMIN]),
    new MainNavItem('/account', 'account.admin', 'manage_accounts', [Roles.ACCOUNT_ADMIN]),
    // new MainNavItem('/address', 'address.title', 'contact_mail', [Roles.ADDRESS_ADMIN]),
    new MainNavItem('/activity', 'activity.title', 'notifications', [Roles.ACTIVITY_ADMIN]),
    new MainNavItem('/category', 'category.type', 'label', [Roles.CATEGORY_ADMIN]),
    new MainNavItem('/settings', 'settings.type', 'settings_applications', [Roles.SETTINGS_ADMIN]),
    new MainNavItem('/mail', 'mail.type', 'email', [Roles.MAIL_ADMIN]),
    new MainNavItem('/cache', 'cache.type', 'memory', [Roles.CACHE_ADMIN]),
    new MainNavItem('/search', 'search.type', 'manage_search', [Roles.SEARCH_ADMIN]),
    new MainNavItem('/backoffice', 'backoffice.type', 'admin_panel_settings', [Roles.BACKOFFICE_ACCESS]),
    new MainNavItem('/history', 'history.type', 'history', [Roles.HISTORY_ADMIN]),
    new MainNavItem('/issue', 'issue.type', 'bug_report', [Roles.ISSUE_ADMIN]),


    // new MainNavItem('/inquiry', 'INQUIRY.Type', 'question_answer'),
    // new MainNavItem('/structure', 'STRUCT.Type', 'ballot'),
    // new MainNavItem('/profile', 'MENU.Profile', 'person'),
    // new MainNavItem('/administration', 'MENU.Administration', 'settings_applications'),
    // new MainNavItem('/imprint', 'MENU.Imprint', 'contact_support'),
  ]

  accessibleItems: MainNavItem[] = []

  constructor(
    public authService: AuthService,
    router: Router,
    private breakpointObserver: BreakpointObserver,
    private changeDetectorRef: ChangeDetectorRef,
    public service: DashboardService,
  ) {
    this.isHandset$ = this.breakpointObserver.observe(Breakpoints.Handset)
      .pipe(map(result => result.matches))

    router.events.pipe(
      withLatestFrom(this.isHandset$),
      filter(([a, b]) => b && a instanceof NavigationEnd)
    ).subscribe(_ => this.drawer?.close())
  }


  ngOnInit() {
    this.accessibleItems = this.navItems.filter(item => item.isAccessible(this.authService))

  }

  ngAfterViewInit() {
    this.changeDetectorRef.detectChanges();
  }


}
