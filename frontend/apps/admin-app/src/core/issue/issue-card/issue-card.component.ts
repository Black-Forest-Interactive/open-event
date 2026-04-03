import { Component, model, inject } from "@angular/core";
import { Issue } from "@open-event/core";
import { DatePipe } from "@angular/common";
import { TranslatePipe } from "@ngx-translate/core";
import { MatDivider } from "@angular/material/divider";
import { MatButton } from "@angular/material/button";
import { IssueService } from "@open-event/admin";
import { HotToastService } from "@ngxpert/hot-toast";

@Component({
  selector: "admin-issue-card",
  imports: [DatePipe, MatButton, MatDivider, TranslatePipe],
  templateUrl: "./issue-card.component.html",
  styleUrl: "./issue-card.component.scss",
})
export class IssueCardComponent {
  private service = inject(IssueService);
  private toast = inject(HotToastService);

  issue = model.required<Issue>();
  loading = false;

  changeStatus(status: string) {
    if (this.loading) return;
    this.loading = true;
    this.service.updateStatus(this.issue().id, status).subscribe({
      next: (value) => this.handleData(value),
      error: (err) => this.handleError(err),
    });
  }

  private handleData(value: Issue) {
    this.issue.set(value);
    this.loading = false;
  }

  private handleError(err: any) {
    if (err) this.toast.error(err);
    this.loading = false;
  }
}
