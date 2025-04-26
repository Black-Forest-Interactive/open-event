import {Component, input, output} from '@angular/core';
import {DatePipe} from "@angular/common";
import {MatTableModule} from "@angular/material/table";
import {MatIconModule} from "@angular/material/icon";
import {MatPaginatorModule, PageEvent} from "@angular/material/paginator";
import {RouterLink} from "@angular/router";
import {TranslatePipe} from "@ngx-translate/core";
import {Activity} from "@open-event-workspace/core";
import {MatButtonModule} from "@angular/material/button";

@Component({
  selector: 'app-activity-table',
  imports: [
    DatePipe,
    MatTableModule,
    MatPaginatorModule,
    MatIconModule,
    MatButtonModule,
    RouterLink,
    TranslatePipe
  ],
  templateUrl: './activity-table.component.html',
  styleUrl: './activity-table.component.scss'
})
export class ActivityTableComponent {

  data = input.required<Activity[]>()
  reloading = input.required<boolean>()
  pageNumber = input.required<number>()
  pageSize = input.required<number>()
  totalElements = input.required<number>()

  pageEvent = output<PageEvent>()
  displayedColumns: string[] = ['title', 'actor', 'source', 'type', 'timestamp', 'cmd']
}
