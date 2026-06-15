import { Component, computed, input } from '@angular/core'
import { EventSearchEntry } from '@open-event/core'
import { RouterLink } from '@angular/router'
import { DatePipe } from '@angular/common'
import { MatIcon } from '@angular/material/icon'
import { MatButton } from '@angular/material/button'
import { MatCard } from '@angular/material/card'
import { TranslatePipe } from '@ngx-translate/core'
import { CategoryChipComponent, getCategoryStyle, RegistrationStatusComponent } from '@open-event/ui'

@Component({
  selector: 'portal-event-card',
  templateUrl: './event-card.component.html',
  styleUrl: './event-card.component.scss',
  imports: [RouterLink, DatePipe, MatIcon, MatButton, MatCard, TranslatePipe, CategoryChipComponent, RegistrationStatusComponent],
  standalone: true
})
export class EventCardComponent {
  entry = input.required<EventSearchEntry>()

  readonly id = computed(() => this.entry().id)
  readonly title = computed(() => this.entry().title)
  readonly shortText = computed(() => this.entry().shortText)
  readonly start = computed(() => this.entry().start)
  readonly finish = computed(() => this.entry().finish)
  readonly hasLocation = computed(() => this.entry().hasLocation)
  readonly street = computed(() => this.entry().street)
  readonly streetNumber = computed(() => this.entry().streetNumber)
  readonly city = computed(() => this.entry().city)
  readonly categories = computed(() => this.entry().categories)
  readonly mediaStyle = computed(() => getCategoryStyle(this.categories()[0] ?? ''))
  readonly hasRegistration = computed(() => this.entry().maxGuestAmount > 0)
}
