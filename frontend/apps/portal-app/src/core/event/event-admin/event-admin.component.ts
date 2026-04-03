import { Component, inject } from '@angular/core'
import { ActivatedRoute, ParamMap } from '@angular/router'
import { MatDialog } from '@angular/material/dialog'
import { Location } from '@angular/common'
import { EventInfo } from '@open-event/core'
import { MatToolbar } from '@angular/material/toolbar'
import { MatIcon } from '@angular/material/icon'
import { EventActionExportComponent } from '../event-action-export/event-action-export.component'
import { RegistrationModerationComponent } from '../../registration/registration-moderation/registration-moderation.component'
import { MatMiniFabButton } from '@angular/material/button'
import { LoadingBarComponent } from '@open-event/shared'
import { EventService } from '@open-event/portal'

@Component({
  selector: 'app-event-admin',
  templateUrl: './event-admin.component.html',
  styleUrl: './event-admin.component.scss',
  imports: [MatToolbar, MatIcon, EventActionExportComponent, RegistrationModerationComponent, MatMiniFabButton, LoadingBarComponent],
  standalone: true
})
export class EventAdminComponent {
  private route = inject(ActivatedRoute);
  private location = inject(Location);
  private service = inject(EventService);
  dialog = inject(MatDialog);

  reloading: boolean = false
  info: EventInfo | undefined
  eventId: number | undefined

  ngOnInit() {
    this.route.paramMap.subscribe((p) => this.handleParams(p))
  }

  reload() {
    if (!this.eventId) return
    if (this.reloading) return
    this.reloading = true
    this.service.getEventInfo(this.eventId).subscribe((d) => this.handleData(d))
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
