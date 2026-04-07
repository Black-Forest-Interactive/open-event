import { Component, computed, inject, input, signal } from '@angular/core'
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
export class EventActionExportComponent {
  private authService = inject(AuthService)
  private service = inject(EventService)

  event = input<Event | undefined>()

  readonly exporting = signal(false)
  readonly accessible = computed(() => this.authService.hasRole(Roles.PERMISSION_EXPORT))

  export() {
    const data = this.event()
    if (!data || this.exporting()) return
    this.exporting.set(true)
    this.service.exportEvent(data.id).subscribe((r) => this.handleExportResponse(r))
  }

  private handleExportResponse(response: HttpResponse<Blob>) {
    download(response)
    this.exporting.set(false)
  }
}
