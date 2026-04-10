import { Component, inject, input } from '@angular/core'
import { EventSearchRequest } from '@open-event/core'
import { ExportService } from '@open-event/admin'
import { HttpResponse } from '@angular/common/http'
import { download } from '@open-event/shared'
import { MatIcon } from '@angular/material/icon'
import { MatMiniFabButton } from '@angular/material/button'
import { MatProgressSpinner } from '@angular/material/progress-spinner'
import { MatTooltip } from '@angular/material/tooltip'
import { TranslatePipe } from '@ngx-translate/core'

@Component({
  selector: 'admin-export-notice-button',
  imports: [MatIcon, MatMiniFabButton, MatProgressSpinner, MatTooltip, TranslatePipe],
  templateUrl: './export-notice-button.component.html',
  styleUrl: './export-notice-button.component.scss'
})
export class ExportNoticeButtonComponent {
  exporting: boolean = false
  request = input.required<EventSearchRequest>()
  private service = inject(ExportService)

  export() {
    if (this.exporting) return
    this.exporting = true
    this.service.exportNotice(this.request()).subscribe((r) => this.handleExportResponse(r))
  }

  private handleExportResponse(response: HttpResponse<Blob>) {
    download(response)
    this.exporting = false
  }
}
