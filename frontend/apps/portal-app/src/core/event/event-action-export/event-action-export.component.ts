import { Component, computed, inject, input, signal } from '@angular/core'
import { HttpResponse } from '@angular/common/http'
import { AuthService, download } from '@open-event/shared'
import { Event } from '@open-event/core'
import { MatIcon } from '@angular/material/icon'
import { MatProgressSpinner } from '@angular/material/progress-spinner'
import { MatIconButton } from '@angular/material/button'
import { MatTooltip } from '@angular/material/tooltip'
import { Roles } from '../../../shared/roles'
import { EventService } from '@open-event/portal'
import { TranslatePipe } from '@ngx-translate/core'

@Component({
  selector: 'portal-event-action-export',
  templateUrl: './event-action-export.component.html',
  styleUrls: ['./event-action-export.component.scss'],
  imports: [MatIcon, MatProgressSpinner, MatIconButton, MatTooltip, TranslatePipe],
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
