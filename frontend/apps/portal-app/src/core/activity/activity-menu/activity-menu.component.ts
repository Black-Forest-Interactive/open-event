import { Component, effect, inject, signal, viewChild } from '@angular/core'

import { MatButton } from '@angular/material/button'
import { MatIcon } from '@angular/material/icon'
import { MatMenu, MatMenuTrigger } from '@angular/material/menu'
import { TranslatePipe } from '@ngx-translate/core'
import { ActivityListComponent } from '../activity-list/activity-list.component'
import { ActivityService } from '@open-event/portal'
import { MatProgressBar } from '@angular/material/progress-bar'
import { ActivityInfo } from '@open-event/core'
import { HotToastService } from '@ngxpert/hot-toast'
import { switchMap } from 'rxjs'

@Component({
  selector: 'portal-activity-menu',
  imports: [MatButton, MatIcon, MatMenu, TranslatePipe, ActivityListComponent, MatProgressBar],
  templateUrl: './activity-menu.component.html',
  styleUrl: './activity-menu.component.scss'
})
export class ActivityMenuComponent {
  private service = inject(ActivityService)
  private toast = inject(HotToastService)
  private menuRef = viewChild<MatMenu>('menu')

  menuTrigger = new MatMenuTrigger()

  readonly data = signal<ActivityInfo[]>([])
  readonly reloading = signal(false)

  constructor() {
    effect(() => {
      const menu = this.menuRef()
      if (menu) this.menuTrigger.menu = menu
    })
  }

  trigger() {
    if (this.menuTrigger.menuOpen) return
    this.menuTrigger.openMenu()
    this.loadData()
  }

  handleMarkReadAll() {
    this.reloading.set(true)
    this.service
      .markReadAll()
      .pipe(switchMap(() => this.service.unreadInfo()))
      .subscribe({
        next: (value) => this.handleData(value),
        error: () => this.handleError()
      })
  }

  handleMarkReadSingle(info: ActivityInfo) {
    if (info.read) return
    this.reloading.set(true)
    this.service
      .markReadSingle(info.activity.id)
      .pipe(switchMap(() => this.service.unreadInfo()))
      .subscribe({
        next: (value) => this.handleData(value),
        error: () => this.handleError()
      })
  }

  private loadData() {
    this.service.unreadInfo().subscribe({
      next: (value) => this.handleData(value),
      error: () => this.handleError()
    })
  }

  private handleData(value: ActivityInfo[]) {
    this.data.set(value)
    this.reloading.set(false)
  }

  private handleError() {
    this.data.set([])
    this.reloading.set(false)
    this.toast.error()
  }
}
