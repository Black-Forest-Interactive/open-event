import {Component, OnInit} from '@angular/core';
import {EventBoardService} from "../model/event-board.service";
import {BreakpointObserver, BreakpointState} from "@angular/cdk/layout";

@Component({
    selector: 'app-event-board',
    templateUrl: './event-board.component.html',
    styleUrls: ['./event-board.component.scss'],
    standalone: false
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
