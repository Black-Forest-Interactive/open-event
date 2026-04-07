import { Component, inject, signal, viewChild } from '@angular/core'
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
  private menu = viewChild<ActivityMenuComponent>('menu')

  private reloading = signal(false)
  private data = signal<ActivityInfo[]>([])
  private unreadInfos = signal(0)

  handleActivityClick(a: ActivityInfo) {
    this.service.markReadSingle(a.activity.id).subscribe({
      next: (value) => this.navigateToSource(value),
      error: () => this.handleError()
    })
  }

  private navigateToSource(activity: Activity) {
    if (activity.source === 'EVENT' || activity.source === 'REGISTRATION') {
      this.router.navigate(['event', 'details', activity.sourceId]).then()
    }
    this.refresh()
  }

  private refresh() {
    if (this.reloading()) return
    this.reloading.set(true)
    this.service.unreadInfo().subscribe({
      next: (value) => this.handleData(value),
      error: () => this.handleError()
    })
  }

  handleMarkAllReadClick() {
    this.reloading.set(true)
    this.service.markReadAll().subscribe({
      next: (value) => this.handleData(value),
      error: () => this.handleError()
    })
  }

  private handleData(value: ActivityInfo[]) {
    this.data.set(value)
    this.unreadInfos.set(value.filter((d) => !d.read).length)
    this.reloading.set(false)
    if (this.unreadInfos() === 0) this.menu()?.menuTrigger.closeMenu()
  }

  private handleError() {
    this.data.set([])
    this.unreadInfos.set(0)
    this.reloading.set(false)
    this.menu()?.menuTrigger.closeMenu()
  }
}
