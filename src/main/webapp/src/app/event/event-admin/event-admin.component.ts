import {Component} from '@angular/core';
import {EventInfo} from "../model/event-api";
import {ActivatedRoute, ParamMap} from "@angular/router";
import {EventService} from "../model/event.service";
import {MatDialog} from "@angular/material/dialog";
import {Location} from "@angular/common";

@Component({
    selector: 'app-event-admin',
    templateUrl: './event-admin.component.html',
    styleUrl: './event-admin.component.scss',
    standalone: false
})
export class EventAdminComponent {

  reloading: boolean = false
  info: EventInfo | undefined
  eventId: number | undefined

  constructor(
    private route: ActivatedRoute,
    private location: Location,
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
  back() {
    this.location.back()
  }
}
