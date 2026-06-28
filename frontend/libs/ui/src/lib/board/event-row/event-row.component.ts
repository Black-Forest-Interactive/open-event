import { Component, computed, input } from '@angular/core'
import { RouterLink } from '@angular/router'
import { DatePipe } from '@angular/common'
import { MatIcon } from '@angular/material/icon'
import { MatButton } from '@angular/material/button'
import { MatCard } from '@angular/material/card'
import { TranslatePipe } from '@ngx-translate/core'
import { CategoryChipComponent } from '../../category/category-chip/category-chip.component'
import { RegistrationStatusComponent } from '../../registration/registration-status/registration-status.component'
import { getCategoryStyle } from '../../category/category-style'
import { EventBoardEntry } from '../event-board.api'

@Component({
  selector: 'lib-event-board-row',
  templateUrl: './event-row.component.html',
  styleUrl: './event-row.component.scss',
  imports: [RouterLink, DatePipe, MatIcon, MatButton, MatCard, TranslatePipe, CategoryChipComponent, RegistrationStatusComponent]
})
export class EventBoardRowComponent {
  data = input.required<EventBoardEntry>()

  readonly link = computed(() => this.data().link)
  readonly title = computed(() => this.data().title)
  readonly shortText = computed(() => this.data().shortText)
  readonly start = computed(() => this.data().start)
  readonly finish = computed(() => this.data().finish)
  readonly hasLocation = computed(() => this.data().hasLocation)
  readonly city = computed(() => this.data().city)
  readonly categories = computed(() => this.data().categories)
  readonly audiences = computed(() => this.data().audiences)
  readonly featured = computed(() => this.data().featured)
  readonly isRegistered = computed(() => this.data().isRegistered)
  readonly hasRegistration = computed(() => this.data().maxGuestAmount > 0)
  readonly mediaStyle = computed(() => getCategoryStyle(this.categories()[0] ?? ''))
}
