import { Component, computed, input } from '@angular/core'
import { EventSearchEntry } from '@open-event/core'
import { RouterLink } from '@angular/router'
import { DatePipe } from '@angular/common'
import { MatIcon } from '@angular/material/icon'
import { MatButton } from '@angular/material/button'
import { MatCard } from '@angular/material/card'
import { CategoryChipComponent, getCategoryStyle, RegistrationStatusComponent } from '@open-event/ui'
import { TranslatePipe } from '@ngx-translate/core'

@Component({
  selector: 'portal-event-row',
  templateUrl: './event-row.component.html',
  styleUrl: './event-row.component.scss',
  imports: [RouterLink, DatePipe, MatIcon, MatButton, MatCard, CategoryChipComponent, RegistrationStatusComponent, TranslatePipe],
  standalone: true
})
export class EventRowComponent {
  entry = input.required<EventSearchEntry>()

  readonly id = computed(() => this.entry().id)
  readonly title = computed(() => this.entry().title)
  readonly shortText = computed(() => this.entry().shortText)
  readonly start = computed(() => this.entry().start)
  readonly finish = computed(() => this.entry().finish)
  readonly hasLocation = computed(() => this.entry().hasLocation)
  readonly city = computed(() => this.entry().city)
  readonly categories = computed(() => this.entry().categories)
  readonly mediaStyle = computed(() => getCategoryStyle(this.categories()[0] ?? ''))
  readonly hasRegistration = computed(() => this.entry().maxGuestAmount > 0)
  readonly isRegistered = computed(() => this.entry().participatingEvent)
}
