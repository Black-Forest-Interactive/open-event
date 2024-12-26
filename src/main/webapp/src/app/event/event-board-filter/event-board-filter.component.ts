import {Component} from '@angular/core';
import {FormControl} from "@angular/forms";
import {EventBoardService} from "../model/event-board.service";
import {ChipSelectEntry} from "../../shared/chip-select-pane/chip-select-entry";
import {CategoryService} from "../../category/model/category.service";
import {MatChipSelectionChange} from "@angular/material/chips";

@Component({
    selector: 'app-event-board-filter',
    templateUrl: './event-board-filter.component.html',
    styleUrl: './event-board-filter.component.scss',
    standalone: false
})
export class EventBoardFilterComponent {


  reloadingCategories: boolean = false
  categoryForm = new FormControl([])
  allCategories: ChipSelectEntry[] = []

  constructor(public service: EventBoardService, private categoryService: CategoryService) {
  }

  onDateRangePickerClosed() {
    if (!this.service.range.valid) return
    this.service.handleRangeChanged()
  }


  selectToday() {
    let now = new Date()
    let start = new Date(now)
    let end = new Date(now)
    this.selectRange(start, end)
  }

  selectThisWeek() {
    let now = new Date()
    let currentDay = now.getDay()

    let start = new Date(now)
    start.setDate(now.getDate() - currentDay)

    let end = new Date(now)
    end.setDate(now.getDate() + (6 - currentDay))
    this.selectRange(start, end)
  }

  selectNextWeek() {
    let now = new Date()
    let currentDay = now.getDay()

    let start = new Date(now)
    start.setDate(now.getDate() - currentDay + 7)

    let end = new Date(now)
    end.setDate(now.getDate() + (6 - currentDay) + 7)
    this.selectRange(start, end)
  }

  private selectRange(start: Date, end: Date) {
    start.setUTCHours(0, 0, 0, 0)
    end.setUTCHours(23, 59, 59, 999)
    this.service.updateRange(start, end)
  }

  handleDatePreselectionChange(event: MatChipSelectionChange) {
    let selected = event.selected
    if (!selected) {
      this.service.updateRange(null, null)
    } else if(event.source.value === 'today') {
      this.selectToday()
    } else if(event.source.value === 'this_week') {
      this.selectThisWeek()
    } else if(event.source.value === 'next_week') {
      this.selectNextWeek()
    }
  }
}
