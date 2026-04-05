import { Component, inject } from '@angular/core'
import { AppLayoutComponent } from '@open-event/ui'
import { TranslatePipe } from '@ngx-translate/core'
import { MatMenu, MatMenuItem, MatMenuTrigger } from '@angular/material/menu'
import { MatIcon } from '@angular/material/icon'
import { MatDivider } from '@angular/material/divider'
import { RouterLink, RouterLinkActive } from '@angular/router'
import { ActivityButtonComponent } from '../../core/activity/activity-button/activity-button.component'
import { AppService } from '../app.service'
import { DashboardService } from './dashboard.service'
import packageJson from '../../../../../package.json'

@Component({
  selector: 'portal-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
  imports: [AppLayoutComponent, TranslatePipe, MatMenu, MatMenuItem, MatMenuTrigger, MatIcon, MatDivider, RouterLink, RouterLinkActive, ActivityButtonComponent],
  standalone: true
})
export class DashboardComponent {
  private appService = inject(AppService)
  private dashboardService = inject(DashboardService)

  title = this.dashboardService.title
  info = this.appService.info
  navGroups = this.dashboardService.accessibleNavGroups
  version = 'V ' + packageJson.version

  logout() {
    this.appService.logout()
  }
}
