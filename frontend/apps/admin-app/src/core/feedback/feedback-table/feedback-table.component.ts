import { Component, input, output, inject } from "@angular/core";
import { MatPaginator, PageEvent } from "@angular/material/paginator";
import { MatDialog } from "@angular/material/dialog";
import { Feedback } from "@open-event/core";
import { FeedbackDetailsDialogComponent } from "../feedback-details-dialog/feedback-details-dialog.component";
import { DatePipe } from "@angular/common";
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef,
  MatRow,
  MatRowDef,
  MatTable,
} from "@angular/material/table";
import { MatIconButton } from "@angular/material/button";
import { TranslatePipe } from "@ngx-translate/core";
import { MatIcon } from "@angular/material/icon";

@Component({
  selector: "admin-feedback-table",
  imports: [
    DatePipe,
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
    MatHeaderCellDef,
  ],
  templateUrl: "./feedback-table.component.html",
  styleUrl: "./feedback-table.component.scss",
})
export class FeedbackTableComponent {
  private dialog = inject(MatDialog);

  data = input.required<Feedback[]>();
  reloading = input.required<boolean>();
  pageNumber = input.required<number>();
  pageSize = input.required<number>();
  totalElements = input.required<number>();

  pageEvent = output<PageEvent>();
  displayedColumns: string[] = [
    "id",
    "subject",
    "topic",
    "account",
    "rating",
    "timestamp",
    "cmd",
  ];

  showDetails(feedback: Feedback) {
    this.dialog.open(FeedbackDetailsDialogComponent, { data: feedback });
  }
}
