import {Component} from '@angular/core';
import {FormControl, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatChipListbox, MatChipOption, MatChipSelectionChange} from "@angular/material/chips";
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
    console.log(`[${new Date().toISOString()}] date picker closed`)
    this.service.handleDatePickerChanged()
  }

  handleDatePreselectionChange(event: MatChipSelectionChange) {
    console.log(`[${new Date().toISOString()}] date preselection changed`)
    const value = event.selected ? event.source.value : undefined
    this.service.handlePreselectionChanged(event.selected, value)
  }

}
