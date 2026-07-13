import { Component, input, output, ChangeDetectionStrategy } from '@angular/core'
import { TranslatePipe } from '@ngx-translate/core'
import { FilterItem } from '../event-board.api'

@Component({
  selector: 'lib-audience-filter',
  templateUrl: './audience-filter.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  imports: [TranslatePipe]
})
export class AudienceFilterComponent {
  items = input.required<FilterItem[]>()
  selected = input.required<Set<string>>()
  toggled = output<string>()
}
