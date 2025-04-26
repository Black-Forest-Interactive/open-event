import {Component} from '@angular/core';
import {FormControl, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatChipListbox, MatChipOption, MatChipSelectionChange} from "@angular/material/chips";
import {DateTime} from "luxon";
import {EventBoardService} from "../event-board.service";
import {ChipSelectEntry} from "../../../../../../libs/shared/src/lib/chip-select-pane/chip-select-entry";
import {MatCard, MatCardContent, MatCardHeader, MatCardTitle} from "@angular/material/card";
import {MatIcon} from "@angular/material/icon";
import {MatDivider} from "@angular/material/divider";
import {MatError, MatFormField, MatFormFieldModule, MatLabel} from "@angular/material/form-field";
import {MatDatepickerModule, MatDatepickerToggle, MatDateRangeInput, MatDateRangePicker} from "@angular/material/datepicker";
import {TranslatePipe} from "@ngx-translate/core";
import {MatProgressBar} from "@angular/material/progress-bar";
import {MatOption, MatSelect} from "@angular/material/select";
import {NgClass, NgForOf} from "@angular/common";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-event-board-filter',
  templateUrl: './event-board-filter.component.html',
  styleUrl: './event-board-filter.component.scss',
  imports: [
    MatCard,
    MatCardHeader,
    MatIcon,
    MatDivider,
    MatCardContent,
    MatFormField,
    MatDateRangeInput,
    ReactiveFormsModule,
    TranslatePipe,
    MatDatepickerToggle,
    MatDateRangePicker,
    MatChipListbox,
    MatChipOption,
    MatProgressBar,
    MatSelect,
    MatOption,
    NgForOf,
    MatButton,
    NgClass,
    MatLabel,
    MatError,
    MatCardTitle,
    MatFormFieldModule, MatDatepickerModule, FormsModule
  ],
  standalone: true
})
export class EventBoardFilterComponent {


  reloadingCategories: boolean = false
  categoryForm = new FormControl([])
  allCategories: ChipSelectEntry[] = []

  constructor(public service: EventBoardService) {
  }

  onDateRangePickerClosed() {
    if (!this.service.range.valid) return
    this.service.handleRangeChanged()
  }


  selectToday() {
    let now = DateTime.now()
    this.selectRange(now, now)
  }

  selectThisWeek() {
    let now = DateTime.now()
    let start = now.startOf('week')
    let end = now.endOf('week')
    this.selectRange(start, end)
  }

  selectNextWeek() {
    let now = DateTime.now()

    let start = now.plus({weeks: 1}).startOf('week')

    let end = now.plus({weeks: 1}).endOf('week')
    this.selectRange(start, end)
  }

  private selectRange(start: DateTime, end: DateTime) {
    this.service.updateRange(start, end)
  }

  handleDatePreselectionChange(event: MatChipSelectionChange) {
    let selected = event.selected
    if (!selected) {
      this.service.updateRange(null, null)
    } else if (event.source.value === 'today') {
      this.selectToday()
    } else if (event.source.value === 'this_week') {
      this.selectThisWeek()
    } else if (event.source.value === 'next_week') {
      this.selectNextWeek()
    }
  }
}
