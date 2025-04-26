import {Component, OnInit} from '@angular/core';
import {BreakpointObserver, BreakpointState} from "@angular/cdk/layout";
import {EventBoardService} from "../event-board.service";
import {EventBoardListComponent} from "../event-board-list/event-board-list.component";
import {EventBoardCalendarComponent} from "../event-board-calendar/event-board-calendar.component";
import {EventBoardTableComponent} from "../event-board-table/event-board-table.component";
import {EventBoardFilterComponent} from "../event-board-filter/event-board-filter.component";
import {EventBoardMapComponent} from "../event-board-map/event-board-map.component";
import {EventBoardHeaderComponent} from "../event-board-header/event-board-header.component";

@Component({
  selector: 'app-event-board',
  templateUrl: './event-board.component.html',
  styleUrls: ['./event-board.component.scss'],
  imports: [
    EventBoardListComponent,
    EventBoardCalendarComponent,
    EventBoardTableComponent,
    EventBoardFilterComponent,
    EventBoardMapComponent,
    EventBoardHeaderComponent
  ],
  standalone: true
})
export class EventBoardComponent implements OnInit {

  mobileView: boolean = false
  mode: string = 'list'

  constructor(public service: EventBoardService, private responsive: BreakpointObserver) {
  }

  ngOnInit() {
    this.responsive
      .observe(['(min-width: 1000px)'])
      .subscribe((state: BreakpointState) => {
        this.mobileView = !state.matches
      })
    this.service.filterToolbarVisible = !this.mobileView
    this.service.infiniteScrollMode = this.mobileView
    this.service.search()
  }

}
