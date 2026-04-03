import { Component, inject, input } from '@angular/core'
import { MatIconButton, MatMiniFabButton } from '@angular/material/button'
import { TranslatePipe } from '@ngx-translate/core'
import { Event, EventSearchEntry } from '@open-event/core'
import { ExportService } from '@open-event/admin'
import { HttpResponse } from '@angular/common/http'
import { download } from '@open-event/shared'
import { MatIcon } from '@angular/material/icon'
import { MatProgressSpinner } from '@angular/material/progress-spinner'
import { MatTooltip } from '@angular/material/tooltip'

@Component({
  selector: 'admin-export-event-button',
  imports: [MatIcon, MatMiniFabButton, MatProgressSpinner, MatTooltip, TranslatePipe, MatIconButton],
  templateUrl: './export-event-button.component.html',
  styleUrl: './export-event-button.component.scss'
})
export class ExportEventButtonComponent {
  private service = inject(ExportService)

  exporting: boolean = false
  event = input.required<Event | EventSearchEntry>()
  type = input('button')

  export() {
    if (this.exporting) return
    this.exporting = true
    this.service.exportEvent(this.event().id).subscribe((r) => this.handleExportResponse(r))
  }

  private handleExportResponse(response: HttpResponse<Blob>) {
    download(response)
    this.exporting = false
  }
}
