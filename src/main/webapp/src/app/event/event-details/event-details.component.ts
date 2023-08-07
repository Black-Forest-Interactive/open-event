import {Component} from '@angular/core';
import {EventService} from "../model/event.service";
import {ActivatedRoute, ParamMap, Router} from "@angular/router";
import {EventInfo} from "../model/event-api";
import {Location} from "@angular/common";
import {HotToastService} from "@ngneat/hot-toast";
import {MatDialog} from "@angular/material/dialog";


@Component({
  selector: 'app-event-details',
  templateUrl: './event-details.component.html',
  styleUrls: ['./event-details.component.scss']
})
export class EventDetailsComponent {

  reloading: boolean = false
  info: EventInfo | undefined


  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private location: Location,
    private service: EventService,
    private toastService: HotToastService,
    public dialog: MatDialog,
  ) {
  }

  ngOnInit() {
    this.route.paramMap.subscribe(p => this.handleParams(p))
  }


  private handleParams(p: ParamMap) {
    let idParam = p.get('id')
    let id = idParam !== null ? +idParam : null
    if (!id) return

    this.reloading = true;
    this.service.getEventInfo(id).subscribe(d => this.handleData(d))

  }

  private handleData(d: EventInfo) {
    this.info = d
    this.reloading = false
  }

}
