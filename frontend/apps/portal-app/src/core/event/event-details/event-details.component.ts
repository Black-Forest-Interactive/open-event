import { Component, computed, inject, resource } from '@angular/core'
import { ActivatedRoute } from '@angular/router'
import { EventDetailsHeaderComponent } from '../event-details-header/event-details-header.component'
import { EventDetailsInfoComponent } from '../event-details-info/event-details-info.component'
import { EventDetailsLocationComponent } from '../event-details-location/event-details-location.component'
import { RegistrationDetailsComponent } from '../../registration/registration-details/registration-details.component'
import { ShareDetailsComponent } from '../../share/share-details/share-details.component'
import { EventService } from '@open-event/portal'
import { LoadingBarComponent, toPromise } from '@open-event/shared'
import { EventDetailsBannerComponent } from '../event-details-banner/event-details-banner.component'
import { MatCard } from '@angular/material/card'
import { toSignal } from '@angular/core/rxjs-interop'
import { map } from 'rxjs/operators'

@Component({
  selector: 'portal-event-details',
  templateUrl: './event-details.component.html',
  styleUrls: ['./event-details.component.scss'],
  imports: [
    EventDetailsHeaderComponent,
    EventDetailsInfoComponent,
    EventDetailsLocationComponent,
    RegistrationDetailsComponent,
    ShareDetailsComponent,
    LoadingBarComponent,
    EventDetailsBannerComponent,
    MatCard
  ],
  standalone: true
})
export class EventDetailsComponent {
  readonly registration = computed(() => this.info()?.registration)
  readonly canEdit = computed(() => this.info()?.canEdit ?? false)
  readonly share = computed(() => this.info()?.share)
  readonly location = computed(() => this.info()?.location)
  private route = inject(ActivatedRoute)
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

  reload() {
    this.infoResource.reload()
  }

  setSharingEnabled(enabled: boolean) {
    const id = this.eventId()
    if (!id) return
    this.service.setShared(id, enabled).subscribe((d) => this.infoResource.set(d))
  }
}
