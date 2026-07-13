import { Component, input, output, ChangeDetectionStrategy } from '@angular/core'
import { TranslatePipe } from '@ngx-translate/core'
import { CategoryChipComponent } from '../../category/category-chip/category-chip.component'
import { FilterItem } from '../event-board.api'

@Component({
  selector: 'lib-category-filter',
  templateUrl: './category-filter.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  imports: [CategoryChipComponent, TranslatePipe]
})
export class CategoryFilterComponent {
  items = input.required<FilterItem[]>()
  selected = input.required<Set<string>>()
  toggled = output<string>()
}
