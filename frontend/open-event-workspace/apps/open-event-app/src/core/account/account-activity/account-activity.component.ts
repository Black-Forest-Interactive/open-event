import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ActivityTableComponent} from "../../activity/activity-table/activity-table.component";

@Component({
  selector: 'app-account-activity',
  imports: [CommonModule, ActivityTableComponent],
  templateUrl: './account-activity.component.html',
  styleUrl: './account-activity.component.scss',
})
export class AccountActivityComponent {}
