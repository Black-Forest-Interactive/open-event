import { Component, computed, inject, input, output, resource } from '@angular/core'
import { CategoryService } from '@open-event/portal'
import { CategoryFilterComponent as LibCategoryFilterComponent } from '@open-event/ui'
import { toPromise } from '@open-event/shared'

@Component({
  selector: 'portal-category-filter',
  templateUrl: './category-filter.component.html',
  imports: [LibCategoryFilterComponent],
  standalone: true
})
export class CategoryFilterComponent {
  selected = input.required<Set<string>>()
  toggle = output<string>()

  private categoryService = inject(CategoryService)

  private categoryResource = resource({
    loader: (p) => toPromise(this.categoryService.getCategories(0, 100), p.abortSignal)
  })
  readonly categories = computed(() => this.categoryResource.value()?.content ?? [])
}
