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
  selector: 'app-dashboard',
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
    new MainNavItem('/event', 'event.type', 'event_note'),
    new MainNavItem('/account', 'account.type', 'person', [Roles.ACCOUNT_READ]),
    new MainNavItem('/address', 'address.title', 'contact_mail', [Roles.ADDRESS_READ]),
    new MainNavItem('/activity', 'activity.title', 'notifications', [Roles.ACTIVITY_READ]),
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
