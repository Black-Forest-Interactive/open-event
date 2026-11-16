import { Component, computed, input, output, ChangeDetectionStrategy } from '@angular/core'
import { MatIcon } from '@angular/material/icon'
import { TranslatePipe } from '@ngx-translate/core'
import { TriFilter } from '@open-event/core'
import { getCategoryStyle } from '../../category/category-style'
import { FilterItem } from '../event-board.api'

@Component({
  selector: 'lib-category-filter',
  templateUrl: './category-filter.component.html',
  styleUrl: './tri-filter.scss',
  changeDetection: ChangeDetectionStrategy.Eager,
  imports: [MatIcon, TranslatePipe]
})
export class CategoryFilterComponent {
  items = input.required<FilterItem[]>()
  state = input.required<TriFilter>()
  label = input('event.filter.category')
  toggled = output<string>()

  readonly entries = computed(() => {
    const state = this.state()
    return this.items().map((item) => ({ item, style: getCategoryStyle(item.name), state: state[item.name] }))
  })
}
