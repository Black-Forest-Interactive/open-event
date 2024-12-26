import {AfterViewInit, ChangeDetectorRef, Component, ViewChild} from '@angular/core';
import {AuthService} from "../../auth/auth.service";
import {NavigationEnd, Router} from "@angular/router";
import {BreakpointObserver, Breakpoints} from "@angular/cdk/layout";
import {filter, map, Observable, withLatestFrom} from "rxjs";
import {MatSidenav} from "@angular/material/sidenav";
import {MainNavItem} from "./main-nav-item";
import {DashboardService} from "../model/dashboard.service";

@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.scss'],
    standalone: false
})
export class DashboardComponent implements AfterViewInit {


  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(map(result => result.matches))


  @ViewChild('drawer') drawer: MatSidenav | undefined

  navItems: MainNavItem[] = [
    new MainNavItem('/event', 'event.type', 'event_note'),
    new MainNavItem('/category', 'category.type', 'label', [AuthService.CATEGORY_WRITE]),
    new MainNavItem('/settings', 'settings.type', 'settings_applications', [AuthService.SETTINGS_READ, AuthService.SETTINGS_WRITE]),
    new MainNavItem('/mail', 'mail.type', 'email', [AuthService.MAIL_READ, AuthService.MAIL_WRITE]),
    new MainNavItem('/cache', 'cache.type', 'memory', [AuthService.CACHE_READ, AuthService.CACHE_WRITE]),
    new MainNavItem('/backoffice', 'backoffice.type', 'admin_panel_settings', [AuthService.BACKOFFICE_ACCESS]),
    new MainNavItem('/history', 'history.type', 'history', [AuthService.HISTORY_ADMIN]),
    new MainNavItem('/account', 'account.type', 'person', [AuthService.ACCOUNT_READ, AuthService.ACCOUNT_ADMIN]),


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
