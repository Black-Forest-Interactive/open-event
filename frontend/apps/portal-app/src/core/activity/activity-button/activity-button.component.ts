import { Component, inject, viewChild } from '@angular/core'
import { Router } from '@angular/router'
import { Activity, ActivityInfo } from '@open-event/core'
import { ActivityIndicatorComponent } from '../activity-indicator/activity-indicator.component'
import { ActivityMenuComponent } from '../activity-menu/activity-menu.component'
import { ActivityService } from '@open-event/portal'

@Component({
  selector: 'portal-activity-button',
  templateUrl: './activity-button.component.html',
  styleUrl: './activity-button.component.scss',
  imports: [ActivityIndicatorComponent, ActivityMenuComponent],
  standalone: true
})
export class ActivityButtonComponent {
  private service = inject(ActivityService)
  private router = inject(Router)

  reloading: boolean = false
  data: ActivityInfo[] = []
  unreadInfos = 0
  private menu = viewChild<ActivityMenuComponent>('menu')

  private reload() {
    if (this.reloading) return
    this.refresh()
  }

  handleActivityClick(a: ActivityInfo) {
    this.service.markReadSingle(a.activity.id).subscribe({
      next: (value) => this.navigateToSource(value),
      error: (err) => this.handleError(err)
    })
  }

  private navigateToSource(activity: Activity) {
    if (activity.source === 'EVENT' || activity.source === 'REGISTRATION') {
      this.router.navigate(['event', 'details', activity.sourceId]).then()
      this.refresh()
    } else {
      this.refresh()
    }
  }

  private refresh() {
    if (this.reloading) return
    this.reloading = true
    this.service.unreadInfo().subscribe({
      next: (value) => this.handleData(value),
      error: (err) => this.handleError(err)
    })
  }

  handleMarkAllReadClick() {
    this.reloading = true
    this.service.markReadAll().subscribe({
      next: (value) => this.handleData(value),
      error: (err) => this.handleError(err)
    })
  }

  private handleData(value: ActivityInfo[]) {
    this.data = value
    this.unreadInfos = this.data.filter((d) => !d.read).length
    this.reloading = false
    if (this.unreadInfos == 0) this.menu()?.menuTrigger.closeMenu()
  }

  private handleError(err: any) {
    this.data = []
    this.unreadInfos = 0
    this.reloading = false
    this.menu()?.menuTrigger.closeMenu()
  }
}
