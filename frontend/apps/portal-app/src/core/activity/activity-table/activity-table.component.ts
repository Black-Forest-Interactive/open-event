import { Component, computed, effect, inject, resource, signal } from '@angular/core'
import { HotToastService } from '@ngxpert/hot-toast'
import { MatPaginator, PageEvent } from '@angular/material/paginator'
import { TranslatePipe } from '@ngx-translate/core'
import { MatIcon } from '@angular/material/icon'
import { MatButton } from '@angular/material/button'
import { ActivityInfo } from '@open-event/core'
import { LoadingBarComponent, toPromise } from '@open-event/shared'
import { ActivityRowComponent } from '../activity-row/activity-row.component'
import { ActivityService } from '@open-event/portal'

@Component({
  selector: 'portal-activity-table',
  templateUrl: './activity-table.component.html',
  styleUrl: './activity-table.component.scss',
  imports: [TranslatePipe, MatIcon, MatButton, MatPaginator, ActivityRowComponent, LoadingBarComponent],
  standalone: true
})
export class ActivityTableComponent {
  // writable so handleReadStatusChanged can patch individual rows without a full reload
  readonly items = signal<ActivityInfo[]>([])
  readonly unread = computed(() => this.items().filter((c) => !c.read).length)
  private service = inject(ActivityService)
  private toast = inject(HotToastService)
  private page = signal(0)
  private size = signal(25)
  private criteria = computed(() => ({ page: this.page(), size: this.size() }))
  private activityResource = resource({
    params: this.criteria,
    loader: (p) => toPromise(this.service.getRecentActivityInfos(p.params.page, p.params.size), p.abortSignal)
  })
  readonly reloading = this.activityResource.isLoading
  readonly totalSize = computed(() => this.activityResource.value()?.totalSize ?? 0)
  readonly pageIndex = computed(() => this.activityResource.value()?.pageable.number ?? 0)
  readonly pageSize = computed(() => this.activityResource.value()?.pageable.size ?? 25)

  constructor() {
    effect(() => {
      const value = this.activityResource.value()
      if (value) this.items.set(value.content)
    })
  }

  handlePageChange(event: PageEvent) {
    this.page.set(event.pageIndex)
    this.size.set(event.pageSize)
  }

  handleReadStatusChanged(event: ActivityInfo) {
    this.items.update((data) => {
      const index = data.findIndex((d) => d.activity.id === event.activity.id)
      if (index < 0) return data
      const updated = [...data]
      updated[index] = event
      return updated
    })
  }

  handleMarkAllReadClick() {
    this.service.markReadAll().subscribe({
      next: () => this.activityResource.reload(),
      error: () => this.toast.error()
    })
  }
}
