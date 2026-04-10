import { Component, computed, input } from '@angular/core'
import { AccountDisplayNamePipe, EventSearchEntry, RegistrationStatusComponent } from '@open-event/core'
import { MatCard } from '@angular/material/card'
import { RouterLink } from '@angular/router'
import { MatIcon } from '@angular/material/icon'
import { DatePipe } from '@angular/common'

@Component({
  selector: 'portal-event-board-list-entry',
  templateUrl: './event-board-list-entry.component.html',
  styleUrl: './event-board-list-entry.component.scss',
  imports: [MatCard, RouterLink, MatIcon, AccountDisplayNamePipe, DatePipe, RegistrationStatusComponent],
  standalone: true
})
export class EventBoardListEntryComponent {
  info = input.required<EventSearchEntry>()

  readonly id = computed(() => this.info().id)
  readonly title = computed(() => this.info().title)
  readonly owner = computed(() => this.info().owner)
  readonly start = computed(() => this.info().start)
  readonly finish = computed(() => this.info().finish)
  readonly hasLocation = computed(() => this.info().hasLocation)
  readonly street = computed(() => this.info().street)
  readonly streetNumber = computed(() => this.info().streetNumber)
  readonly zip = computed(() => this.info().zip)
  readonly city = computed(() => this.info().city)
  readonly categories = computed(() => this.info().categories)
  readonly tags = computed(() => this.info().tags)
}
