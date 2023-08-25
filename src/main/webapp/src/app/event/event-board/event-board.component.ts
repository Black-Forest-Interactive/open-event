import {Component} from '@angular/core';
import {EventBoardService} from "../model/event-board.service";
import {BreakpointObserver, BreakpointState} from "@angular/cdk/layout";

@Component({
  selector: 'app-event-board',
  templateUrl: './event-board.component.html',
  styleUrls: ['./event-board.component.scss']
})
export class EventBoardComponent {

  mobileView: boolean = false

  constructor(public service: EventBoardService, private responsive: BreakpointObserver) {
  }

  ngOnInit() {
    this.service.reload()

    this.responsive
      .observe(['(min-width: 1000px)'])
      .subscribe((state: BreakpointState) => {
        this.mobileView = !state.matches
      })
  }

}
