import { Component, computed, inject, resource, signal, ChangeDetectionStrategy } from '@angular/core'
import { toPromise } from '@open-event/shared'
import { EventService } from '@open-event/admin'
import { ActivatedRoute } from '@angular/router'
import { Location } from '@angular/common'
import { BoardComponent, BoardToolbarActions } from '../../../shared/board/board.component'
import { EventDetailsActionsComponent } from '../event-details-actions/event-details-actions.component'
import { EventPublishButtonComponent } from '../event-publish-button/event-publish-button.component'
import { TranslatePipe } from '@ngx-translate/core'
import { EventDetailsRegistrationComponent } from '../event-details-registration/event-details-registration.component'
import { EventDetailsHistoryComponent } from '../event-details-history/event-details-history.component'
import { EventDetailsLocationComponent } from '../event-details-location/event-details-location.component'
import { EventDetailsInfoComponent } from '../event-details-info/event-details-info.component'
import { MatCard } from '@angular/material/card'
import { RegistrationStatusComponent } from '@open-event/ui'

@Component({
  selector: 'admin-event-details',
  imports: [
    BoardComponent,
    BoardToolbarActions,
    EventDetailsActionsComponent,
    EventPublishButtonComponent,
    TranslatePipe,
    EventDetailsRegistrationComponent,
    EventDetailsHistoryComponent,
    EventDetailsLocationComponent,
    EventDetailsInfoComponent,
    MatCard,
    RegistrationStatusComponent
  ],
  templateUrl: './event-details.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './event-details.component.scss'
})
export class EventDetailsComponent {
  id = signal(-1)
  private service = inject(EventService)
  private route = inject(ActivatedRoute)
  private location = inject(Location)
  private eventResource = resource({
    params: this.id,
    loader: (param) => toPromise(this.service.getEventInfo(param.params), param.abortSignal)
  })
  readonly event = computed(this.eventResource.value ?? undefined)
  readonly loading = this.eventResource.isLoading
  readonly error = this.eventResource.error

  readonly registration = computed(() => this.event()?.registration)
  private readonly participants = computed(() => this.registration()?.participants ?? [])
  readonly maxGuestAmount = computed(() => this.registration()?.registration.maxGuestAmount ?? 0)
  readonly confirmedParticipants = computed(() => this.participants().filter((p) => !p.waitingList))
  readonly waitingParticipants = computed(() => this.participants().filter((p) => p.waitingList))
  readonly confirmedCount = computed(() => this.confirmedParticipants().length)
  readonly confirmedPeople = computed(() => this.confirmedParticipants().reduce((sum, p) => sum + p.size, 0))
  readonly waitingCount = computed(() => this.waitingParticipants().length)
  readonly waitingPeople = computed(() => this.waitingParticipants().reduce((sum, p) => sum + p.size, 0))

  constructor() {
    this.route.paramMap.subscribe((params) => {
      const id = params.get('id')
      if (id) this.id.set(+id)
    })
  }

  back() {
    this.location.back()
  }

  reload() {
    this.eventResource.reload()
  }
}
