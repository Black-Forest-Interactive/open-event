import { Component, signal } from "@angular/core";

import { MatCard } from "@angular/material/card";
import { FeedbackChangeRequest, FeedbackFormComponent } from "@open-event/core";
import { FeedbackService } from "@open-event/app";
import { TranslatePipe, TranslateService } from "@ngx-translate/core";
import { MatDivider } from "@angular/material/divider";
import { LoadingBarComponent } from "@open-event/shared";
import { HotToastService } from "@ngxpert/hot-toast";

@Component({
  selector: "app-feedback",
  imports: [
    MatCard,
    FeedbackFormComponent,
    TranslatePipe,
    MatDivider,
    LoadingBarComponent,
  ],
  templateUrl: "./feedback.component.html",
  styleUrl: "./feedback.component.scss",
})
export class FeedbackComponent {
  loading = signal(false);

  constructor(
    private service: FeedbackService,
    private toast: HotToastService,
    private translate: TranslateService,
  ) {}

  submit(request: FeedbackChangeRequest) {
    debugger;
    if (this.loading()) return;
    this.loading.set(true);
    this.service.createFeedback(request).subscribe({
      next: (result) =>
        this.toast.success(this.translate.instant("feedback.confirmation")),
      error: (error) =>
        this.toast.error(this.translate.instant("feedback.error")),
      complete: () => this.loading.set(false),
    });
  }
}
