import {Component, input, output} from '@angular/core';
import {Event} from "@open-event-workspace/core";
import {HotToastService} from "@ngxpert/hot-toast";
import {EventService} from "@open-event-workspace/backoffice";
import {MatIcon} from "@angular/material/icon";
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {MatMiniFabButton} from "@angular/material/button";

@Component({
  selector: 'app-event-publish-button',
  imports: [
    MatIcon,
    MatProgressSpinner,
    MatMiniFabButton
  ],
  templateUrl: './event-publish-button.component.html',
  styleUrl: './event-publish-button.component.scss'
})
export class EventPublishButtonComponent {
  data = input.required<Event>()

  publishing = false
  changed = output<Event>()

  constructor(
    private service: EventService,
    private toastService: HotToastService
  ) {
  }

  publish() {
    if (this.publishing) return
    this.publishing = true
    this.service.publish(this.data().id).subscribe({
      next: d => {
        this.changed.emit(d)
        this.publishing = false
      },
      error: err => {
        this.toastService.error(err)
        this.publishing = false
      }
    })
  }

}
