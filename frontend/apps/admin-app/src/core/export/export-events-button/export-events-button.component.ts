import { Component, input, inject } from '@angular/core'
import { MatMiniFabButton } from '@angular/material/button'
import { HttpResponse } from '@angular/common/http'
import { ExportService } from '@open-event/admin'
import { download } from '@open-event/shared'
import { MatIcon } from '@angular/material/icon'
import { MatProgressSpinner } from '@angular/material/progress-spinner'
import { EventSearchRequest } from '@open-event/core'
import { MatTooltip } from '@angular/material/tooltip'
import { TranslatePipe } from '@ngx-translate/core'

@Component({
  selector: 'admin-export-events-button',
  imports: [MatMiniFabButton, MatIcon, MatProgressSpinner, MatTooltip, TranslatePipe],
  templateUrl: './export-events-button.component.html',
  styleUrl: './export-events-button.component.scss'
})
export class ExportEventsButtonComponent {
  private service = inject(ExportService)

  exporting: boolean = false
  request = input.required<EventSearchRequest>()

  export() {
    if (this.exporting) return
    this.exporting = true
    this.service.exportEvents(this.request()).subscribe((r) => this.handleExportResponse(r))
  }

  private handleExportResponse(response: HttpResponse<Blob>) {
    download(response)
    this.exporting = false
  }
}
