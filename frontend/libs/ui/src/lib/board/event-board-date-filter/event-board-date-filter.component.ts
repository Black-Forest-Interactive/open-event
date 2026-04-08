import { Component, output, signal } from '@angular/core'
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms'
import { MatCard } from '@angular/material/card'
import { MatIcon } from '@angular/material/icon'
import { MatDivider } from '@angular/material/divider'
import { MatFormField, MatFormFieldModule, MatLabel, MatError } from '@angular/material/form-field'
import { MatDatepickerModule, MatDatepickerToggle, MatDateRangeInput, MatDateRangePicker } from '@angular/material/datepicker'
import { MatChipListbox, MatChipOption, MatChipSelectionChange } from '@angular/material/chips'
import { MatButton } from '@angular/material/button'
import { TranslatePipe } from '@ngx-translate/core'
import { DateTime } from 'luxon'
import { EventBoardDateRange } from './event-board-date-filter.model'

@Component({
  selector: 'lib-event-board-date-filter',
  templateUrl: './event-board-date-filter.component.html',
  imports: [
    MatCard, MatIcon, MatDivider,
    MatFormField, MatLabel, MatError, MatFormFieldModule, MatDatepickerModule,
    MatDateRangeInput, MatDatepickerToggle, MatDateRangePicker,
    MatChipListbox, MatChipOption, MatButton,
    ReactiveFormsModule, TranslatePipe
  ]
})
export class EventBoardDateFilterComponent {
  rangeChanged = output<EventBoardDateRange>()
  filterReset = output()

  readonly preselection = signal<string | undefined>(undefined)

  readonly range = new FormGroup({
    start: new FormControl<DateTime | null>(null),
    end: new FormControl<DateTime | null>(null)
  })

  onDateRangePickerClosed() {
    if (!this.range.valid) return
    this.preselection.set(undefined)
    const { start, end } = this.range.value
    this.emit(start ?? null, end ?? null)
  }

  handlePreselectionChange(event: MatChipSelectionChange) {
    const value: string = event.source.value
    if (!event.selected) {
      this.preselection.set(undefined)
      this.applyRange(null, null)
      return
    }
    if (this.preselection() === value) return
    this.preselection.set(value)
    if (value === 'today') {
      const now = DateTime.now()
      this.applyRange(now, now)
    } else if (value === 'this_week') {
      const now = DateTime.now()
      this.applyRange(now.startOf('week'), now.endOf('week'))
    } else if (value === 'next_week') {
      const next = DateTime.now().plus({ weeks: 1 })
      this.applyRange(next.startOf('week'), next.endOf('week'))
    }
  }

  onReset() {
    this.preselection.set(undefined)
    this.range.setValue({ start: null, end: null })
    this.filterReset.emit()
  }

  private applyRange(start: DateTime | null, end: DateTime | null) {
    this.range.setValue({ start, end })
    this.emit(start, end)
  }

  private emit(start: DateTime | null, end: DateTime | null) {
    this.rangeChanged.emit({
      start: start?.startOf('day').toISODate() ?? undefined,
      end: end?.endOf('day').toISODate() ?? undefined
    })
  }
}
