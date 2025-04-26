import {Component, input, output} from '@angular/core';
import {Category} from "@open-event-workspace/core";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {MatCell, MatCellDef, MatColumnDef, MatHeaderCell, MatHeaderCellDef, MatHeaderRow, MatHeaderRowDef, MatRow, MatRowDef, MatTable} from "@angular/material/table";
import {MatIcon} from "@angular/material/icon";
import {MatIconAnchor, MatIconButton} from "@angular/material/button";
import {TranslatePipe} from "@ngx-translate/core";

@Component({
  selector: 'app-category-table',
  imports: [
    MatCell,
    MatCellDef,
    MatColumnDef,
    MatHeaderCell,
    MatHeaderRow,
    MatHeaderRowDef,
    MatIcon,
    MatIconButton,
    MatPaginator,
    MatRow,
    MatRowDef,
    MatTable,
    TranslatePipe,
    MatIconAnchor,
    MatHeaderCellDef
  ],
  templateUrl: './category-table.component.html',
  styleUrl: './category-table.component.scss'
})
export class CategoryTableComponent {
  data = input.required<Category[]>()
  reloading = input.required<boolean>()
  pageNumber = input.required<number>()
  pageSize = input.required<number>()
  totalElements = input.required<number>()

  pageEvent = output<PageEvent>()
  editEvent = output<Category>()
  deleteEvent = output<Category>()
  displayedColumns: string[] = ['name', 'cmd']
}
