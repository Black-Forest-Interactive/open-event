import { Component, input, output } from '@angular/core'
import { EventBoardDateFilterComponent, EventBoardDateRange } from '@open-event/ui'
import { MatIcon } from '@angular/material/icon'
import { MatSlideToggle } from '@angular/material/slide-toggle'
import { MatButton } from '@angular/material/button'
import { TranslatePipe } from '@ngx-translate/core'
import { CategoryFilterComponent } from './category-filter/category-filter.component'
import { AudienceFilterComponent } from './audience-filter/audience-filter.component'

@Component({
  selector: 'portal-event-board-filter',
  templateUrl: './event-board-filter.component.html',
  styleUrl: './event-board-filter.component.scss',
  imports: [EventBoardDateFilterComponent, MatIcon, MatSlideToggle, MatButton, TranslatePipe, CategoryFilterComponent, AudienceFilterComponent],
  standalone: true
})
export class EventBoardFilterComponent {
  isDiscover = input.required<boolean>()
  categoryFilter = input.required<Set<string>>()
  audienceFilter = input.required<Set<string>>()
  showHistory = input.required<boolean>()
  showAvailableOnly = input.required<boolean>()

  dateRangeChanged = output<EventBoardDateRange>()
  toggleHistory = output<void>()
  toggleCategory = output<string>()
  toggleAudience = output<string>()
  toggleAvailable = output<void>()
  resetFilter = output<void>()
}
