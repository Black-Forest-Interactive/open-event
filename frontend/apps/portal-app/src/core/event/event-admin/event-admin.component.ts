import { Component, computed, inject, resource } from '@angular/core'
import { ActivatedRoute } from '@angular/router'
import { Location } from '@angular/common'
import { MatToolbar } from '@angular/material/toolbar'
import { MatIcon } from '@angular/material/icon'
import { EventActionExportComponent } from '../event-action-export/event-action-export.component'
import { RegistrationModerationComponent } from '../../registration/registration-moderation/registration-moderation.component'
import { MatIconButton } from '@angular/material/button'
import { MatTooltip } from '@angular/material/tooltip'
import { LoadingBarComponent, toPromise } from '@open-event/shared'
import { EventService } from '@open-event/portal'
import { toSignal } from '@angular/core/rxjs-interop'
import { map } from 'rxjs/operators'
import { TranslatePipe } from '@ngx-translate/core'

@Component({
  selector: 'portal-event-admin',
  templateUrl: './event-admin.component.html',
  styleUrl: './event-admin.component.scss',
  imports: [MatToolbar, MatIcon, EventActionExportComponent, RegistrationModerationComponent, MatIconButton, MatTooltip, LoadingBarComponent, TranslatePipe],
  standalone: true
})
export class EventAdminComponent {
  private route = inject(ActivatedRoute)
  private location = inject(Location)
  private service = inject(EventService)

  private eventId = toSignal(
    this.route.paramMap.pipe(
      map((p) => {
        const id = p.get('id')
        return id ? +id : undefined
      })
    )
  )

  private infoResource = resource({
    params: this.eventId,
    loader: (p) => (p.params ? toPromise(this.service.getEventInfo(p.params), p.abortSignal) : Promise.resolve(undefined))
  })

  readonly info = computed(() => this.infoResource.value())
  readonly reloading = this.infoResource.isLoading
  readonly event = computed(() => this.info()?.event)
  readonly registration = computed(() => this.info()?.registration)

  back() {
    this.location.back()
  }
}
