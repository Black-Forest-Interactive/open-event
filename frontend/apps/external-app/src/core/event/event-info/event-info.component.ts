import { Component, computed, input } from '@angular/core'
import { PublicEvent } from '@open-event/external'
import { DatePipe } from '@angular/common'
import { MatIcon } from '@angular/material/icon'
import { TranslatePipe } from '@ngx-translate/core'
import { AvatarComponent, CategoryChipComponent, getCategoryStyle } from '@open-event/ui'

@Component({
  selector: 'app-event-info',
  imports: [DatePipe, MatIcon, TranslatePipe, AvatarComponent, CategoryChipComponent],
  templateUrl: './event-info.component.html',
  styleUrl: './event-info.component.scss'
})
export class EventInfoComponent {
  event = input.required<PublicEvent>()

  readonly title = computed(() => this.event().title)
  readonly shortText = computed(() => this.event().shortText)
  readonly longText = computed(() => this.event().longText)
  readonly start = computed(() => this.event().start)
  readonly finish = computed(() => this.event().finish)
  readonly ownerName = computed(() => this.event().owner.name)
  readonly hasLocation = computed(() => this.event().hasLocation)
  readonly zip = computed(() => this.event().zip)
  readonly city = computed(() => this.event().city)
  readonly country = computed(() => this.event().country)
  readonly categories = computed(() => this.event().categories)
  readonly tags = computed(() => this.event().tags)
  readonly mediaStyle = computed(() => getCategoryStyle(this.categories()[0] ?? ''))
  readonly durationMinutes = computed(() => Math.round((new Date(this.finish()).getTime() - new Date(this.start()).getTime()) / 60000))
}
