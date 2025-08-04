import {Component, input} from '@angular/core';
import {download} from "@open-event-workspace/shared";
import {HttpResponse} from "@angular/common/http";
import {ExportService} from "@open-event-workspace/backoffice";
import {MatMiniFabButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {EventSearchRequest} from "@open-event-workspace/core";
import {TranslatePipe} from "@ngx-translate/core";
import {MatTooltip} from "@angular/material/tooltip";

@Component({
  selector: 'app-export-summary-button',
  imports: [
    MatMiniFabButton,
    MatIcon,
    MatProgressSpinner,
    MatTooltip,
    TranslatePipe
  ],
  templateUrl: './export-summary-button.component.html',
  styleUrl: './export-summary-button.component.scss'
})
export class ExportSummaryButtonComponent {

  summarizing: boolean = false
  request = input.required<EventSearchRequest>()

  constructor(
    private service: ExportService,
  ) {
  }

  exportSummary() {
    if (this.summarizing) return
    this.summarizing = true
    this.service.exportSummary(this.request()).subscribe(r => this.handleExportResponse(r))
  }

  private handleExportResponse(response: HttpResponse<Blob>) {
    download(response)
    this.summarizing = false
  }
}
