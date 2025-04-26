import {Component, computed, resource, signal, viewChild} from '@angular/core';
import {toPromise} from "@open-event-workspace/shared";
import {EventService} from "@open-event-workspace/backoffice";
import {ActivatedRoute} from "@angular/router";
import {Location} from "@angular/common";
import {BoardComponent, BoardToolbarActions} from "../../../shared/board/board.component";
import {EventMenuComponent} from "../event-menu/event-menu.component";
import {EventPublishButtonComponent} from "../event-publish-button/event-publish-button.component";
import {MatIcon} from "@angular/material/icon";
import {MatMiniFabButton} from "@angular/material/button";
import {MatTab, MatTabGroup, MatTabLabel} from "@angular/material/tabs";
import {TranslatePipe} from "@ngx-translate/core";
import {EventDetailsRegistrationComponent} from "../event-details-registration/event-details-registration.component";
import {EventDetailsHistoryComponent} from "../event-details-history/event-details-history.component";
import {EventDetailsLocationComponent} from "../event-details-location/event-details-location.component";
import {EventDetailsInfoComponent} from "../event-details-info/event-details-info.component";

@Component({
  selector: 'app-event-details',
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
    EventDetailsInfoComponent
  ],
  templateUrl: './event-details.component.html',
  styleUrl: './event-details.component.scss'
})
export class EventDetailsComponent {
  id = signal(-1)

  eventResource = resource({
    request: this.id,
    loader: (param) => {
      return toPromise(this.service.getEventInfo(param.request))
    }
  })


  event = computed(this.eventResource.value ?? undefined)
  loading = this.eventResource.isLoading
  error = this.eventResource.error

  menu = viewChild.required<EventMenuComponent>('menu')


  constructor(private service: EventService, private route: ActivatedRoute, private location: Location) {

    this.route.paramMap.subscribe(params => {
      let id = params.get('id')!
      this.id.set(+id)
    })

  }

  back() {
    this.location.back()
  }
}
