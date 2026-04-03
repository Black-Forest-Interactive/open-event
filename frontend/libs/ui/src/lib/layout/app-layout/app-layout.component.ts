import { Component, computed, effect, inject, input, output, signal, ViewChild } from '@angular/core'
import { toSignal } from '@angular/core/rxjs-interop'
import { NavigationEnd, Router, RouterOutlet } from '@angular/router'
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout'
import { filter } from 'rxjs'
import { MatSidenav, MatSidenavContainer, MatSidenavContent } from '@angular/material/sidenav'
import { MatIconButton } from '@angular/material/button'
import { MatIcon } from '@angular/material/icon'
import { TranslatePipe } from '@ngx-translate/core'
import { NavGroup } from '../nav.api'
import { AppToolbarComponent } from '../app-toolbar/app-toolbar.component'
import { AppSidenavComponent } from '../app-sidenav/app-sidenav.component'
import { AppFooterComponent } from '../app-footer/app-footer.component'

@Component({
  selector: 'lib-app-layout',
  templateUrl: './app-layout.component.html',
  styleUrl: './app-layout.component.scss',
  standalone: true,
  imports: [RouterOutlet, MatSidenavContainer, MatSidenav, MatSidenavContent, MatIconButton, MatIcon, TranslatePipe, AppToolbarComponent, AppSidenavComponent, AppFooterComponent]
})
export class AppLayoutComponent {
  private readonly breakpointObserver = inject(BreakpointObserver)
  private readonly router = inject(Router)

  navGroups = input<NavGroup[]>([])
  title = input<string>('')
  version = input<string>('')
  footerInfo = input<string>('')

  logoutClick = output<void>()

  @ViewChild('sidenav') sidenav: MatSidenav | undefined

  private readonly breakpointResult = toSignal(this.breakpointObserver.observe([Breakpoints.Handset, Breakpoints.TabletPortrait]))
  private readonly navigationEnd = toSignal(this.router.events.pipe(filter((e) => e instanceof NavigationEnd)))

  readonly isHandset = computed(() => this.breakpointResult()?.matches ?? false)
  readonly sidenavCollapsed = signal(false)
  readonly sidenavMode = computed<'over' | 'side'>(() => this.isHandset() ? 'over' : 'side')
  readonly sidenavOpened = computed(() => !this.isHandset())

  constructor() {
    effect(() => { if (this.isHandset()) this.sidenavCollapsed.set(false) })
    effect(() => { this.navigationEnd(); if (this.isHandset()) this.sidenav?.close() })
  }

  toggleCollapse() {
    this.sidenavCollapsed.update((v) => !v)
  }
}
