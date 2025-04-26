import {Component, model} from '@angular/core';
import {Issue} from '@open-event-workspace/core';
import {DatePipe} from "@angular/common";
import {TranslatePipe} from "@ngx-translate/core";
import {MatDivider} from "@angular/material/divider";
import {MatButton} from "@angular/material/button";
import {IssueService} from "@open-event-workspace/backoffice";
import {HotToastService} from "@ngxpert/hot-toast";

@Component({
  selector: 'app-issue-card',
  imports: [
    DatePipe,
    MatButton,
    MatDivider,
    TranslatePipe
  ],
  templateUrl: './issue-card.component.html',
  styleUrl: './issue-card.component.scss'
})
export class IssueCardComponent {
  issue = model.required<Issue>()
  loading = false

  constructor(private service: IssueService, private toast: HotToastService) {
  }

  changeStatus(status: string) {
    if (this.loading) return
    this.loading = true
    this.service.updateStatus(this.issue().id, status).subscribe({
      next: value => this.handleData(value),
      error: err => this.handleError(err)
    })
  }

  private handleData(value: Issue) {
    this.issue.set(value)
    this.loading = false
  }

  private handleError(err: any) {
    if (err) this.toast.error(err)
    this.loading = false
  }
}
