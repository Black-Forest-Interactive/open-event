import { Component, computed, inject, resource } from '@angular/core'
import { ReactiveFormsModule } from '@angular/forms'
import { EventBoardService } from '../event-board.service'
import { CategoryService } from '@open-event/portal'
import { CategoryChipComponent } from '@open-event/ui'
import { toPromise } from '@open-event/shared'
import { MatCard } from '@angular/material/card'
import { MatIcon } from '@angular/material/icon'
import { MatDivider } from '@angular/material/divider'
import { MatError, MatFormField, MatFormFieldModule, MatLabel } from '@angular/material/form-field'
import { MatDatepickerModule, MatDatepickerToggle, MatDateRangeInput, MatDateRangePicker } from '@angular/material/datepicker'
import { MatSlideToggle } from '@angular/material/slide-toggle'
import { TranslatePipe } from '@ngx-translate/core'
import { MatButton } from '@angular/material/button'

@Component({
  selector: 'portal-event-board-filter',
  templateUrl: './event-board-filter.component.html',
  styleUrl: './event-board-filter.component.scss',
  imports: [
    MatCard,
    MatIcon,
    MatDivider,
    MatFormField,
    MatLabel,
    MatError,
    MatFormFieldModule,
    MatDatepickerModule,
    MatDateRangeInput,
    MatDatepickerToggle,
    MatDateRangePicker,
    ReactiveFormsModule,
    MatSlideToggle,
    MatButton,
    CategoryChipComponent,
    TranslatePipe
  ],
  standalone: true
})
export class EventBoardFilterComponent {
  protected service = inject(EventBoardService)
  private categoryService = inject(CategoryService)

  readonly whenOptions = [
    { value: 'any', labelKey: 'event.filter.when.any', icon: 'all_inclusive' },
    { value: 'today', labelKey: 'event.filter.when.today', icon: 'today' },
    { value: 'weekend', labelKey: 'event.filter.when.weekend', icon: 'weekend' },
    { value: 'next_week', labelKey: 'event.filter.when.nextWeek', icon: 'next_week' }
  ]

  private categoryResource = resource({
    loader: (param) => toPromise(this.categoryService.getCategories(0, 100), param.abortSignal)
  })
  readonly categories = computed(() => this.categoryResource.value()?.content ?? [])

  isWhenActive(value: string) {
    const preselection = this.service.preselection()
    if (value === 'any') return preselection === undefined || preselection === 'any'
    return preselection === value
  }

  selectWhen(value: string) {
    this.service.handlePreselectionChanged(true, value)
  }

  onDateRangePickerClosed() {
    if (!this.service.range.valid) return
    this.service.handleDatePickerChanged()
  }
}
