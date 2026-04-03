import { Component, computed, effect, resource, signal, viewChild, inject } from '@angular/core'
import { toPromise } from '@open-event/shared'
import { EventService } from '@open-event/admin'
import { ActivatedRoute } from '@angular/router'
import { Location } from '@angular/common'
import { BoardComponent, BoardToolbarActions } from '../../../shared/board/board.component'
import { EventMenuComponent } from '../event-menu/event-menu.component'
import { EventPublishButtonComponent } from '../event-publish-button/event-publish-button.component'
import { MatIcon } from '@angular/material/icon'
import { MatMiniFabButton } from '@angular/material/button'
import { MatTab, MatTabGroup, MatTabLabel } from '@angular/material/tabs'
import { TranslatePipe } from '@ngx-translate/core'
import { EventDetailsRegistrationComponent } from '../event-details-registration/event-details-registration.component'
import { EventDetailsHistoryComponent } from '../event-details-history/event-details-history.component'
import { EventDetailsLocationComponent } from '../event-details-location/event-details-location.component'
import { EventDetailsInfoComponent } from '../event-details-info/event-details-info.component'
import { ExportEventButtonComponent } from '../../export/export-event-button/export-event-button.component'

@Component({
  selector: 'admin-event-details',
  imports: [
    BoardComponent,
    BoardToolbarActions,
    EventMenuComponent,
    EventPublishButtonComponent,
    MatIcon,
    MatMiniFabButton,
    MatMiniFabButton,
    MatTabGroup,
    MatTab,
    TranslatePipe,
    EventDetailsRegistrationComponent,
    EventDetailsHistoryComponent,
    MatTabLabel,
    EventDetailsLocationComponent,
    EventDetailsInfoComponent,
    ExportEventButtonComponent
  ],
  templateUrl: './event-details.component.html',
  styleUrl: './event-details.component.scss'
})
export class EventDetailsComponent {
  private service = inject(EventService)
  private route = inject(ActivatedRoute)
  private location = inject(Location)

  id = signal(-1)

  private eventResource = resource({
    params: this.id,
    loader: (param) => toPromise(this.service.getEventInfo(param.params), param.abortSignal)
  })

  readonly event = computed(this.eventResource.value ?? undefined)
  readonly loading = this.eventResource.isLoading
  readonly error = this.eventResource.error

  menu = viewChild.required<EventMenuComponent>('menu')

  constructor() {
    this.route.paramMap.subscribe((params) => {
      const id = params.get('id')!
      this.id.set(+id)
    })

    effect(() => {
      this.menu().reload.subscribe((e) => this.eventResource.reload())
    })
  }

  back() {
    this.location.back()
  }
}
