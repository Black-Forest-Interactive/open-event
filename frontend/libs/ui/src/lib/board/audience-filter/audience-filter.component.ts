import { Component, input, output } from '@angular/core'
import { TranslatePipe } from '@ngx-translate/core'
import { FilterItem } from '../event-board.api'

@Component({
  selector: 'lib-audience-filter',
  templateUrl: './audience-filter.component.html',
  imports: [TranslatePipe]
})
export class AudienceFilterComponent {
  items = input.required<FilterItem[]>()
  selected = input.required<Set<string>>()
  toggle = output<string>()
}
