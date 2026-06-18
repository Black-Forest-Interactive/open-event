import { Component, computed, input } from '@angular/core'
import { MatIcon } from '@angular/material/icon'
import { getCategoryStyle } from '../category-style'

@Component({
  selector: 'lib-category-chip',
  imports: [MatIcon],
  templateUrl: './category-chip.component.html',
  styleUrl: './category-chip.component.scss'
})
export class CategoryChipComponent {
  name = input.required<string>()
  iconUrl = input<string>('')
  size = input<'sm' | 'md'>('md')

  readonly style = computed(() => getCategoryStyle(this.name()))
}
