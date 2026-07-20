import { Component, computed, inject, input, resource, ChangeDetectionStrategy } from '@angular/core'
import { MatDivider } from '@angular/material/divider'
import { DatePipe } from '@angular/common'
import { TranslatePipe } from '@ngx-translate/core'
import { AnnouncementService } from '@open-event/portal'
import { LoadingBarComponent, toPromise } from '@open-event/shared'

@Component({
  selector: 'portal-event-announcements',
  templateUrl: './event-announcements.component.html',
  imports: [MatDivider, DatePipe, TranslatePipe, LoadingBarComponent],
  changeDetection: ChangeDetectionStrategy.Eager
})
export class EventAnnouncementsComponent {
  private service = inject(AnnouncementService)

  eventId = input.required<number>()

  private announcementsResource = resource({
    params: this.eventId,
    loader: (p) => toPromise(this.service.getAnnouncements(p.params, 0, 5), p.abortSignal)
  })

  readonly announcements = computed(() => this.announcementsResource.value()?.content ?? [])
  readonly loading = this.announcementsResource.isLoading

  reload() {
    this.announcementsResource.reload()
  }
}
