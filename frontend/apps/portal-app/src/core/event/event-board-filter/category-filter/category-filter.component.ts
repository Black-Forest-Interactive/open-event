import { Component, computed, inject, input, output, resource } from '@angular/core'
import { Category } from '@open-event/core'
import { CategoryService } from '@open-event/portal'
import { CategoryChipComponent } from '@open-event/ui'
import { toPromise } from '@open-event/shared'
import { TranslatePipe } from '@ngx-translate/core'

@Component({
  selector: 'portal-category-filter',
  templateUrl: './category-filter.component.html',
  imports: [CategoryChipComponent, TranslatePipe],
  standalone: true
})
export class CategoryFilterComponent {
  selected = input.required<Set<string>>()
  toggle = output<string>()

  private categoryService = inject(CategoryService)

  private categoryResource = resource({
    loader: (p) => toPromise(this.categoryService.getCategories(0, 100), p.abortSignal)
  })
  readonly categories = computed<Category[]>(() => this.categoryResource.value()?.content ?? [])
}
