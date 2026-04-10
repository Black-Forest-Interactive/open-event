import { Component, computed, input } from '@angular/core'
import { MatCard } from '@angular/material/card'
import { MatIcon } from '@angular/material/icon'
import { DatePipe } from '@angular/common'
import { RouterLink } from '@angular/router'
import { EventCardEntry } from './event-card.api'

@Component({
  selector: 'lib-event-card',
  templateUrl: './event-card.component.html',
  imports: [MatCard, MatIcon, DatePipe, RouterLink]
})
export class EventCardComponent {
  data = input.required<EventCardEntry>()

  readonly link = computed(() => this.data().link)
  readonly title = computed(() => this.data().title)
  readonly ownerName = computed(() => this.data().ownerName)
  readonly start = computed(() => this.data().start)
  readonly finish = computed(() => this.data().finish)
  readonly hasLocation = computed(() => this.data().hasLocation)
  readonly street = computed(() => this.data().street ?? '')
  readonly streetNumber = computed(() => this.data().streetNumber ?? '')
  readonly zip = computed(() => this.data().zip ?? '')
  readonly city = computed(() => this.data().city ?? '')
  readonly categories = computed(() => this.data().categories)
  readonly tags = computed(() => this.data().tags)
}
