import {Component} from '@angular/core';
import {EventService} from "../model/event.service";
import {ActivatedRoute, ParamMap} from "@angular/router";
import {EventInfo} from "../model/event-api";
import {MatDialog} from "@angular/material/dialog";


@Component({
  selector: 'app-event-details',
  templateUrl: './event-details.component.html',
  styleUrls: ['./event-details.component.scss']
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

  private handleParams(p: ParamMap) {
    let idParam = p.get('id')
    this.eventId = idParam !== null ? +idParam : undefined
    this.reload()
  }

  reload() {
    if (!this.eventId) return
    if (this.reloading) return
    this.reloading = true
    this.service.getEventInfo(this.eventId).subscribe(d => this.handleData(d))
  }

  private handleData(d: EventInfo) {
    this.info = d
    this.reloading = false
  }

}
