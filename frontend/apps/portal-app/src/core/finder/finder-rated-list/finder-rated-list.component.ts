import { ChangeDetectionStrategy, Component, input, output } from '@angular/core'
import { DatePipe } from '@angular/common'
import { MatButton } from '@angular/material/button'
import { MatButtonToggle, MatButtonToggleGroup } from '@angular/material/button-toggle'
import { MatIcon } from '@angular/material/icon'
import { MatTooltip } from '@angular/material/tooltip'
import { TranslatePipe } from '@ngx-translate/core'
import { EventSearchEntry, FinderAnswer } from '@open-event/core'
import { CategoryChipComponent, getCategoryStyle } from '@open-event/ui'
import { FINDER_ANSWER_ICONS } from '../finder.util'

export interface FinderRateEvent {
  entry: EventSearchEntry
  answer: FinderAnswer
}

@Component({
  selector: 'portal-finder-rated-list',
  templateUrl: './finder-rated-list.component.html',
  styleUrl: './finder-rated-list.component.scss',
  imports: [DatePipe, MatButton, MatButtonToggle, MatButtonToggleGroup, MatIcon, MatTooltip, TranslatePipe, CategoryChipComponent],
  changeDetection: ChangeDetectionStrategy.Eager
})
export class FinderRatedListComponent {
  entries = input.required<EventSearchEntry[]>()
  answer = input.required<FinderAnswer>()

  rate = output<FinderRateEvent>()
  register = output<EventSearchEntry>()
  openEvent = output<EventSearchEntry>()

  readonly answers: FinderAnswer[] = ['YES', 'MAYBE', 'NO']
  readonly icons = FINDER_ANSWER_ICONS
  readonly style = getCategoryStyle
}
