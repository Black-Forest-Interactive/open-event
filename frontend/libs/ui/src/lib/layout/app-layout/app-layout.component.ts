import { Component, computed, effect, inject, input, output, signal, ViewChild } from '@angular/core'
import { toSignal } from '@angular/core/rxjs-interop'
import { NavigationEnd, Router, RouterOutlet } from '@angular/router'
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout'
import { filter } from 'rxjs'
import { MatSidenav, MatSidenavContainer, MatSidenavContent } from '@angular/material/sidenav'
import { NavGroup } from '../nav.api'
import { AppToolbarComponent } from '../app-toolbar/app-toolbar.component'
import { AppSidenavComponent } from '../app-sidenav/app-sidenav.component'
import { AppFooterComponent } from '../app-footer/app-footer.component'

@Component({
  selector: 'lib-app-layout',
  templateUrl: './app-layout.component.html',
  styleUrl: './app-layout.component.scss',
  standalone: true,
  imports: [RouterOutlet, MatSidenavContainer, MatSidenav, MatSidenavContent, AppToolbarComponent, AppSidenavComponent, AppFooterComponent]
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
  readonly sidenavOpen = signal(true)
  readonly sidenavMode = computed<'over' | 'side'>(() => (this.isHandset() ? 'over' : 'side'))

  constructor() {
    effect(() => {
      if (this.isHandset()) {
        this.sidenavCollapsed.set(false)
        this.sidenavOpen.set(false)
      } else {
        this.sidenavOpen.set(true)
      }
    })
    effect(() => {
      this.navigationEnd()
      if (this.isHandset()) this.sidenavOpen.set(false)
    })
  }

  toggleSidenav() {
    this.sidenavOpen.update((v) => !v)
  }

  toggleCollapse() {
    this.sidenavCollapsed.update((v) => !v)
  }
}
