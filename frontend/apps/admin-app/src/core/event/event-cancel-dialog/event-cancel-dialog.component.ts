import { Component, inject, signal, ChangeDetectionStrategy } from '@angular/core'
import { FormsModule } from '@angular/forms'
import { EventService } from '@open-event/admin'
import { MAT_DIALOG_DATA, MatDialogActions, MatDialogClose, MatDialogContent, MatDialogRef, MatDialogTitle } from '@angular/material/dialog'
import { Event } from '@open-event/core'
import { MatButton } from '@angular/material/button'
import { MatFormField, MatLabel } from '@angular/material/form-field'
import { MatInput } from '@angular/material/input'
import { TranslatePipe, TranslateService } from '@ngx-translate/core'
import { HotToastService } from '@ngxpert/hot-toast'

@Component({
  selector: 'admin-event-cancel-dialog',
  imports: [FormsModule, MatDialogTitle, MatDialogContent, MatDialogActions, MatButton, MatDialogClose, MatFormField, MatLabel, MatInput, TranslatePipe],
  templateUrl: './event-cancel-dialog.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './event-cancel-dialog.component.scss'
})
export class EventCancelDialogComponent {
  dialogRef = inject<MatDialogRef<EventCancelDialogComponent>>(MatDialogRef)
  data = inject<Event>(MAT_DIALOG_DATA)
  private service = inject(EventService)
  private toast = inject(HotToastService)
  private translation = inject(TranslateService)

  readonly reason = signal('')
  readonly cancelling = signal(false)

  cancel() {
    if (this.cancelling() || !this.reason().trim()) return
    this.cancelling.set(true)
    this.service.cancel(this.data.id, this.reason()).subscribe({
      next: () => {
        this.cancelling.set(false)
        this.dialogRef.close(true)
        this.translation.get('event.message.cancel.succeed').subscribe((t) => this.toast.success(t))
      },
      error: () => {
        this.cancelling.set(false)
        this.translation.get('event.message.error').subscribe((t) => this.toast.error(t))
      }
    })
  }
}
