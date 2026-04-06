import { Component, inject } from '@angular/core'
import { ReactiveFormsModule } from '@angular/forms'
import { MatChipListbox, MatChipOption, MatChipSelectionChange } from '@angular/material/chips'
import { EventBoardService } from '../event-board.service'
import { MatCard } from '@angular/material/card'
import { MatIcon } from '@angular/material/icon'
import { MatDivider } from '@angular/material/divider'
import { MatError, MatFormField, MatFormFieldModule, MatLabel } from '@angular/material/form-field'
import { MatDatepickerModule, MatDatepickerToggle, MatDateRangeInput, MatDateRangePicker } from '@angular/material/datepicker'
import { TranslatePipe } from '@ngx-translate/core'
import { MatButton } from '@angular/material/button'

@Component({
  selector: 'portal-event-board-filter',
  templateUrl: './event-board-filter.component.html',
  styleUrl: './event-board-filter.component.scss',
  imports: [
    MatCard, MatIcon, MatDivider,
    MatFormField, MatLabel, MatError, MatFormFieldModule, MatDatepickerModule,
    MatDateRangeInput, MatDatepickerToggle, MatDateRangePicker, ReactiveFormsModule,
    MatChipListbox, MatChipOption, MatButton, TranslatePipe
  ],
  standalone: true
})
export class EventBoardFilterComponent {
  service = inject(EventBoardService)

  onDateRangePickerClosed() {
    if (!this.service.range.valid) return
    this.service.handleDatePickerChanged()
  }

  handleDatePreselectionChange(event: MatChipSelectionChange) {
    const value = event.selected ? event.source.value : undefined
    this.service.handlePreselectionChanged(event.selected, value)
  }
}
