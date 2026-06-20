import { Component, computed, input } from '@angular/core'
import { PublicEvent } from '@open-event/external'
import { RouterLink } from '@angular/router'
import { DatePipe } from '@angular/common'
import { MatIcon } from '@angular/material/icon'
import { MatButton } from '@angular/material/button'
import { MatCard } from '@angular/material/card'
import { TranslatePipe } from '@ngx-translate/core'
import { CategoryChipComponent, getCategoryStyle, RegistrationStatusComponent } from '@open-event/ui'

@Component({
  selector: 'app-event-row',
  templateUrl: './event-row.component.html',
  styleUrl: './event-row.component.scss',
  imports: [RouterLink, DatePipe, MatIcon, MatButton, MatCard, TranslatePipe, CategoryChipComponent, RegistrationStatusComponent]
})
export class EventRowComponent {
  event = input.required<PublicEvent>()

  readonly key = computed(() => this.event().key)
  readonly title = computed(() => this.event().title)
  readonly shortText = computed(() => this.event().shortText)
  readonly start = computed(() => this.event().start)
  readonly finish = computed(() => this.event().finish)
  readonly hasLocation = computed(() => this.event().hasLocation)
  readonly city = computed(() => this.event().city)
  readonly categories = computed(() => this.event().categories)
  readonly mediaStyle = computed(() => getCategoryStyle(this.categories()[0] ?? ''))
  readonly hasRegistration = computed(() => this.event().maxGuestAmount > 0)
}
