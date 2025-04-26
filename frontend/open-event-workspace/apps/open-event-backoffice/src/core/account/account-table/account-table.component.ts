import {Component, input, output} from '@angular/core';
import {AccountSearchEntry} from "@open-event-workspace/core";
import {MatPaginatorModule, PageEvent} from "@angular/material/paginator";
import {MatTableModule} from "@angular/material/table";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {TranslatePipe} from "@ngx-translate/core";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-account-table',
  imports: [
    MatIconModule,
    MatButtonModule,
    MatPaginatorModule,
    MatTableModule,
    TranslatePipe,
    RouterLink
  ],
  templateUrl: './account-table.component.html',
  styleUrl: './account-table.component.scss'
})
export class AccountTableComponent {
  data = input.required<AccountSearchEntry[]>()
  reloading = input.required<boolean>()
  pageNumber = input.required<number>()
  pageSize = input.required<number>()
  totalElements = input.required<number>()

  pageEvent = output<PageEvent>()
  createEvent = output<AccountSearchEntry>()
  editEvent = output<AccountSearchEntry>()
  deleteEvent = output<AccountSearchEntry>()
  displayedColumns: string[] = ['name', 'firstname', 'lastname', 'email', 'phone', 'mobile', 'cmd']
}
