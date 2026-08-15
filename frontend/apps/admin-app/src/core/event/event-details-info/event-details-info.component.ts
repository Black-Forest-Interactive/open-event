import { Component, computed, input, ChangeDetectionStrategy } from '@angular/core'
import { EventInfo } from '@open-event/core'
import { AvatarComponent, CategoryChipComponent, EventPublishedIconComponent } from '@open-event/ui'
import { TranslatePipe } from '@ngx-translate/core'
import { DatePipe } from '@angular/common'
import { MatIcon } from '@angular/material/icon'
import { MatButton } from '@angular/material/button'
import { MatCard } from '@angular/material/card'

@Component({
  selector: 'admin-event-details-info',
  imports: [TranslatePipe, EventPublishedIconComponent, CategoryChipComponent, AvatarComponent, DatePipe, MatIcon, MatButton, MatCard],
  templateUrl: './event-details-info.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './event-details-info.component.scss'
})
export class EventDetailsInfoComponent {
  event = input.required<EventInfo>()

  readonly title = computed(() => this.event().event.title)
  readonly shortText = computed(() => this.event().event.shortText)
  readonly longText = computed(() => this.event().event.longText)
  readonly tags = computed(() => this.event().event.tags)
  readonly categories = computed(() => this.event().categories)
  readonly created = computed(() => this.event().event.created)
  readonly changed = computed(() => this.event().event.changed)
  readonly owner = computed(() => this.event().event.owner)
  readonly hasLocation = computed(() => this.event().event.hasLocation)
  readonly location = computed(() => this.event().location)
  readonly start = computed(() => this.event().event.start)
  readonly finish = computed(() => this.event().event.finish)

  readonly mailtoOwner = computed(() => `mailto:${this.owner().email}?subject=${encodeURIComponent(this.title())}`)

  readonly durationMinutes = computed(() => {
    const start = new Date(this.start()).getTime()
    const finish = new Date(this.finish()).getTime()
    return Math.max(0, Math.round((finish - start) / 60000))
  })
}
