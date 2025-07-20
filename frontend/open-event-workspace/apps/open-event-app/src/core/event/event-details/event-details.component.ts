import {Component} from '@angular/core';
import {ActivatedRoute, ParamMap} from "@angular/router";
import {EventInfo} from "@open-event-workspace/core";
import {MatDialog} from "@angular/material/dialog";
import {EventDetailsHeaderComponent} from "../event-details-header/event-details-header.component";
import {EventDetailsInfoComponent} from "../event-details-info/event-details-info.component";
import {EventDetailsLocationComponent} from "../event-details-location/event-details-location.component";
import {RegistrationDetailsComponent} from "../../registration/registration-details/registration-details.component";
import {ShareDetailsComponent} from "../../share/share-details/share-details.component";
import {EventService} from "@open-event-workspace/app";
import {LoadingBarComponent} from "@open-event-workspace/shared";
import {EventDetailsBannerComponent} from "../event-details-banner/event-details-banner.component";
import {MatCard} from "@angular/material/card";
import {MatDivider} from "@angular/material/divider";


@Component({
  selector: 'app-event-details',
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
    MatCard,
    MatDivider
  ],
  standalone: true
})
export class EventDetailsComponent {

  reloading: boolean = false
  info: EventInfo | undefined
  eventId: number | undefined

  constructor(
    private route: ActivatedRoute,
    private service: EventService,
    public dialog: MatDialog,
  ) {
  }

  ngOnInit() {
    this.route.paramMap.subscribe(p => this.handleParams(p))
  }

  reload() {
    if (!this.eventId) return
    if (this.reloading) return
    this.reloading = true
    this.service.getEventInfo(this.eventId).subscribe(d => this.handleData(d))
  }

  private handleParams(p: ParamMap) {
    let idParam = p.get('id')
    this.eventId = idParam !== null ? +idParam : undefined
    this.reload()
  }

  private handleData(d: EventInfo) {
    this.info = d
    this.reloading = false
  }

  setSharingEnabled(enabled: boolean) {
    const eventId = this.eventId
    if (!eventId || this.reloading) return
    this.reloading = true
    this.service.setShared(eventId, enabled).subscribe(d => this.handleData(d))
  }


}
