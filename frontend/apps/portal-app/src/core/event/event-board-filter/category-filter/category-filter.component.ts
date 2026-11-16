import { Component, computed, inject, input, output, resource, ChangeDetectionStrategy } from '@angular/core'
import { CategoryService } from '@open-event/portal'
import { CategoryFilterComponent as LibCategoryFilterComponent } from '@open-event/ui'
import { toPromise } from '@open-event/shared'
import { TriFilter } from '@open-event/core'

@Component({
  selector: 'portal-category-filter',
  templateUrl: './category-filter.component.html',
  imports: [LibCategoryFilterComponent],
  changeDetection: ChangeDetectionStrategy.Eager
})
export class CategoryFilterComponent {
  state = input.required<TriFilter>()
  label = input<string>()
  toggled = output<string>()

  private categoryService = inject(CategoryService)

  private categoryResource = resource({
    loader: (p) => toPromise(this.categoryService.getCategories(0, 100), p.abortSignal)
  })
  readonly categories = computed(() => this.categoryResource.value()?.content ?? [])
}
