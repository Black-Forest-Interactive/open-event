import { Component, computed, input, output } from '@angular/core'
import { MatIcon } from '@angular/material/icon'
import { Category } from '@open-event/core'
import { getCategoryStyle } from '../category-style'

@Component({
  selector: 'lib-category-picker',
  imports: [MatIcon],
  templateUrl: './category-picker.component.html',
  styleUrl: './category-picker.component.scss'
})
export class CategoryPickerComponent {
  categories = input.required<Category[]>()
  selectedIds = input.required<number[]>()

  toggled = output<number>()

  readonly entries = computed(() =>
    this.categories().map((category) => ({
      category,
      style: getCategoryStyle(category.name),
      selected: this.selectedIds().includes(category.id)
    }))
  )
}
