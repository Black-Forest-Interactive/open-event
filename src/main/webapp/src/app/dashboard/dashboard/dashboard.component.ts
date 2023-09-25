import {AfterViewInit, ChangeDetectorRef, Component, ViewChild} from '@angular/core';
import {AuthService} from "../../auth/auth.service";
import {MatSelectChange} from "@angular/material/select";
import {TranslateService} from "@ngx-translate/core";
import {NavigationEnd, Router} from "@angular/router";
import {BreakpointObserver, Breakpoints} from "@angular/cdk/layout";
import {filter, map, Observable, withLatestFrom} from "rxjs";
import {MatSidenav} from "@angular/material/sidenav";
import {MainNavItem} from "./main-nav-item";
import {MatDialog} from "@angular/material/dialog";
import {ConfirmLogoutDialogComponent} from "../confirm-logout-dialog/confirm-logout-dialog.component";
import {DashboardService} from "../model/dashboard.service";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements AfterViewInit {

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(map(result => result.matches));


  lang: string = 'de'
  collapsed: boolean = true
  @ViewChild('drawer') drawer: MatSidenav | undefined
  navItems: MainNavItem[] = [
    new MainNavItem('/event', 'event.type', 'event_note'),
    new MainNavItem('/category', 'category.type', 'label', [AuthService.CATEGORY_WRITE]),
    new MainNavItem('/settings', 'settings.type', 'settings_applications', [AuthService.SETTINGS_READ, AuthService.SETTINGS_WRITE]),
    new MainNavItem('/mail', 'mail.type', 'email', [AuthService.MAIL_READ, AuthService.MAIL_WRITE]),
    new MainNavItem('/cache', 'cache.type', 'memory', [AuthService.CACHE_READ, AuthService.CACHE_WRITE]),
    new MainNavItem('/backoffice', 'backoffice.type', 'admin_panel_settings', [AuthService.BACKOFFICE_ACCESS]),
    new MainNavItem('/history', 'history.type', 'history', [AuthService.HISTORY_ADMIN]),


    // new MainNavItem('/inquiry', 'INQUIRY.Type', 'question_answer'),
    // new MainNavItem('/structure', 'STRUCT.Type', 'ballot'),
    // new MainNavItem('/profile', 'MENU.Profile', 'person'),
    // new MainNavItem('/administration', 'MENU.Administration', 'settings_applications'),
    // new MainNavItem('/imprint', 'MENU.Imprint', 'contact_support'),
  ];


  accessibleItems: MainNavItem[] = [];

  constructor(
    public authService: AuthService,
    private translate: TranslateService,
    router: Router,
    private breakpointObserver: BreakpointObserver,
    private changeDetectorRef: ChangeDetectorRef,
    private dialog: MatDialog,
    public service: DashboardService
  ) {
    translate.setDefaultLang('en')
    translate.use(this.lang)

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

  changeLang(event: MatSelectChange) {
    let value = event.value;
    this.translate.use(value)
  }

  selectLang(value: string) {
    this.lang = value;
    this.translate.use(this.lang)
  }

  logout() {
    const dialogRef = this.dialog.open(ConfirmLogoutDialogComponent, {
      width: '250px',
      data: ''
    })
    dialogRef.afterClosed().subscribe(result => {
      if (result) this.authService.logout()
    })
  }

  toggleCollapsed() {
    this.collapsed = !this.collapsed;
  }
}
