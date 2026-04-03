import { Component, inject, input, OnInit } from '@angular/core'
import { HttpResponse } from '@angular/common/http'
import { AuthService, download } from '@open-event/shared'
import { Event } from '@open-event/core'
import { MatIcon } from '@angular/material/icon'
import { MatProgressSpinner } from '@angular/material/progress-spinner'
import { MatMiniFabButton } from '@angular/material/button'
import { Roles } from '../../../shared/roles'
import { EventService } from '@open-event/portal'

@Component({
  selector: 'portal-event-action-export',
  templateUrl: './event-action-export.component.html',
  styleUrls: ['./event-action-export.component.scss'],
  imports: [MatIcon, MatProgressSpinner, MatMiniFabButton],
  standalone: true
})
export class EventActionExportComponent implements OnInit {
  private authService = inject(AuthService)
  private service = inject(EventService)

  event = input<Event | undefined>()
  accessible: boolean = false
  exporting: boolean = false

  ngOnInit() {
    this.accessible = this.authService.hasRole(Roles.PERMISSION_EXPORT)
  }

  export() {
    const data = this.event()
    if (!data) return
    if (this.exporting) return
    this.exporting = true
    this.service.exportEvent(data.id).subscribe((r) => this.handleExportResponse(r))
  }

  private handleExportResponse(response: HttpResponse<Blob>) {
    download(response)
    this.exporting = false
  }
}
