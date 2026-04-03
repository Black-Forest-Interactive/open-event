import { Component, inject, input, output } from '@angular/core'
import { Event } from '@open-event/core'
import { HotToastService } from '@ngxpert/hot-toast'
import { EventService } from '@open-event/admin'
import { MatIcon } from '@angular/material/icon'
import { MatProgressSpinner } from '@angular/material/progress-spinner'
import { MatMiniFabButton } from '@angular/material/button'

@Component({
  selector: 'admin-event-publish-button',
  imports: [MatIcon, MatProgressSpinner, MatMiniFabButton],
  templateUrl: './event-publish-button.component.html',
  styleUrl: './event-publish-button.component.scss'
})
export class EventPublishButtonComponent {
  private service = inject(EventService)
  private toastService = inject(HotToastService)

  data = input.required<Event>()

  publishing = false
  changed = output<Event>()

  publish() {
    if (this.publishing) return
    this.publishing = true
    this.service.publish(this.data().id).subscribe({
      next: (d) => {
        this.changed.emit(d)
        this.publishing = false
      },
      error: () => {
        this.toastService.error()
        this.publishing = false
      }
    })
  }
}
