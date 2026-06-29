import { Component, inject, signal } from '@angular/core'
import { FormsModule } from '@angular/forms'
import { Router } from '@angular/router'
import { MAT_DIALOG_DATA, MatDialogActions, MatDialogClose, MatDialogContent, MatDialogRef, MatDialogTitle } from '@angular/material/dialog'
import { MatButton } from '@angular/material/button'
import { MatFormField, MatLabel } from '@angular/material/form-field'
import { MatInput } from '@angular/material/input'
import { TranslatePipe, TranslateService } from '@ngx-translate/core'
import { HotToastService } from '@ngxpert/hot-toast'
import { Event } from '@open-event/core'
import { AnnouncementChangeRequest, AnnouncementService, EventService } from '@open-event/portal'

@Component({
  selector: 'portal-event-cancel-dialog',
  templateUrl: './event-cancel-dialog.component.html',
  imports: [FormsModule, MatDialogTitle, MatDialogContent, MatDialogActions, MatButton, MatDialogClose, MatFormField, MatLabel, MatInput, TranslatePipe],
  standalone: true
})
export class EventCancelDialogComponent {
  private data = inject<{ event: Event; participantCount: number }>(MAT_DIALOG_DATA)
  private dialogRef = inject(MatDialogRef<EventCancelDialogComponent>)
  private announcementService = inject(AnnouncementService)
  private eventService = inject(EventService)
  private router = inject(Router)
  private toast = inject(HotToastService)
  private translateService = inject(TranslateService)

  readonly event = this.data.event
  readonly participantCount = this.data.participantCount
  readonly reason = signal('')
  readonly cancelling = signal(false)

  cancel() {
    if (this.cancelling() || !this.reason().trim()) return
    this.cancelling.set(true)

    const notifyAndDelete = () => {
      this.eventService.deleteEvent(this.event.id).subscribe({
        next: () => {
          this.cancelling.set(false)
          this.dialogRef.close(true)
          this.translateService.get('event.cancel.message.done').subscribe((t) => this.toast.success(t))
          this.router.navigate(['/event/own'])
        },
        error: () => {
          this.cancelling.set(false)
          this.translateService.get('action.error').subscribe((t) => this.toast.error(t))
        }
      })
    }

    if (this.participantCount > 0) {
      this.translateService.get('event.cancel.title').subscribe((subject) => {
        this.announcementService.createAnnouncement(this.event.id, new AnnouncementChangeRequest(subject, this.reason())).subscribe({
          next: () => notifyAndDelete(),
          error: () => notifyAndDelete()
        })
      })
    } else {
      notifyAndDelete()
    }
  }
}
