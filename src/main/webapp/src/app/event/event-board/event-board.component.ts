import {Component} from '@angular/core';
import {EventBoardService} from "../model/event-board.service";
import {BreakpointObserver, Breakpoints} from "@angular/cdk/layout";
import {map, Observable} from "rxjs";

@Component({
  selector: 'app-event-board',
  templateUrl: './event-board.component.html',
  styleUrls: ['./event-board.component.scss']
})
export class EventBoardComponent {

  isHandset$: Observable<boolean> = this.responsive.observe(Breakpoints.Web)
    .pipe(map(result => result.matches));

  constructor(public service: EventBoardService, private responsive: BreakpointObserver) {
  }

  ngOnInit() {
    this.service.reload()
  }

}
