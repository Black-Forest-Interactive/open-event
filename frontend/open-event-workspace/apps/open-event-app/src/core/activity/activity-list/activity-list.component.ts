import {Component, input, output} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ActivityInfo} from "@open-event-workspace/core";
import {TranslatePipe} from "@ngx-translate/core";
import {MatDivider} from "@angular/material/divider";
import {MatIcon} from "@angular/material/icon";

@Component({
  selector: 'app-activity-list',
  imports: [CommonModule, TranslatePipe, MatDivider, MatIcon],
  templateUrl: './activity-list.component.html',
  styleUrl: './activity-list.component.scss',
})
export class ActivityListComponent {

  data = input.required<ActivityInfo[]>()
  clicked = output<ActivityInfo>()

}
