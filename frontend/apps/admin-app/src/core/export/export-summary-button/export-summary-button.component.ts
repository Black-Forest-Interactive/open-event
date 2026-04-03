import { Component, input, inject } from "@angular/core";
import { download } from "@open-event/shared";
import { HttpResponse } from "@angular/common/http";
import { ExportService } from "@open-event/admin";
import { MatMiniFabButton } from "@angular/material/button";
import { MatIcon } from "@angular/material/icon";
import { MatProgressSpinner } from "@angular/material/progress-spinner";
import { EventSearchRequest } from "@open-event/core";
import { TranslatePipe } from "@ngx-translate/core";
import { MatTooltip } from "@angular/material/tooltip";

@Component({
  selector: "admin-export-summary-button",
  imports: [
    MatMiniFabButton,
    MatIcon,
    MatProgressSpinner,
    MatTooltip,
    TranslatePipe,
  ],
  templateUrl: "./export-summary-button.component.html",
  styleUrl: "./export-summary-button.component.scss",
})
export class ExportSummaryButtonComponent {
  private service = inject(ExportService);

  summarizing: boolean = false;
  request = input.required<EventSearchRequest>();

  exportSummary() {
    if (this.summarizing) return;
    this.summarizing = true;
    this.service
      .exportSummary(this.request())
      .subscribe((r) => this.handleExportResponse(r));
  }

  private handleExportResponse(response: HttpResponse<Blob>) {
    download(response);
    this.summarizing = false;
  }
}
