import { Component, input, output, ChangeDetectionStrategy } from '@angular/core'
import { MatIcon } from '@angular/material/icon'
import { TranslatePipe } from '@ngx-translate/core'
import { TriFilter } from '@open-event/core'
import { FilterItem } from '../event-board.api'

@Component({
  selector: 'lib-audience-filter',
  templateUrl: './audience-filter.component.html',
  styleUrl: '../category-filter/tri-filter.scss',
  changeDetection: ChangeDetectionStrategy.Eager,
  imports: [MatIcon, TranslatePipe]
})
export class AudienceFilterComponent {
  items = input.required<FilterItem[]>()
  state = input.required<TriFilter>()
  label = input('event.filter.audience')
  toggled = output<string>()
}
