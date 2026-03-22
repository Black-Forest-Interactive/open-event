import {Component} from '@angular/core';
import {ActivityTableComponent} from "../activity-table/activity-table.component";

@Component({
  selector: 'app-activity-board',
  imports: [
    ActivityTableComponent
  ],
  templateUrl: './activity-board.component.html',
  styleUrl: './activity-board.component.scss'
})
export class ActivityBoardComponent {

}
