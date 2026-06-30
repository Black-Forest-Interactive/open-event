import { Component, inject, linkedSignal, resource, signal } from '@angular/core'
import { FormsModule } from '@angular/forms'
import { MAT_DIALOG_DATA, MatDialogActions, MatDialogClose, MatDialogContent, MatDialogRef, MatDialogTitle } from '@angular/material/dialog'
import { MatButton } from '@angular/material/button'
import { MatFormField, MatHint, MatLabel } from '@angular/material/form-field'
import { MatInput } from '@angular/material/input'
import { TranslatePipe, TranslateService } from '@ngx-translate/core'
import { HotToastService } from '@ngxpert/hot-toast'
import { EventUpdateTextRequest } from '@open-event/core'
import { EventService } from '@open-event/portal'
import { LoadingBarComponent, toPromise } from '@open-event/shared'

@Component({
  selector: 'portal-event-text-edit-dialog',
  templateUrl: './event-text-edit-dialog.component.html',
  styleUrl: './event-text-edit-dialog.component.scss',
  imports: [FormsModule, MatDialogTitle, MatDialogContent, MatDialogActions, MatDialogClose, MatFormField, MatLabel, MatHint, MatInput, MatButton, TranslatePipe, LoadingBarComponent],
  standalone: true
})
export class EventTextEditDialogComponent {
  private data = inject<{ id: number }>(MAT_DIALOG_DATA)
  readonly dialogRef = inject(MatDialogRef<EventTextEditDialogComponent>)
  private service = inject(EventService)
  private toast = inject(HotToastService)
  private translate = inject(TranslateService)

  private eventResource = resource({ loader: (p) => toPromise(this.service.getEvent(this.data.id), p.abortSignal) })
  readonly loading = this.eventResource.isLoading
  readonly saving = signal(false)

  readonly title = linkedSignal(() => this.eventResource.value()?.title ?? '')
  readonly shortText = linkedSignal(() => this.eventResource.value()?.shortText ?? '')
  readonly longText = linkedSignal(() => this.eventResource.value()?.longText ?? '')

  save() {
    if (this.saving()) return
    this.saving.set(true)
    this.service.setText(this.data.id, new EventUpdateTextRequest(this.title(), this.shortText(), this.longText())).subscribe({
      next: () => {
        this.translate.get('event.message.update.succeed').subscribe(msg => this.toast.success(msg))
        this.saving.set(false)
        this.dialogRef.close(true)
      },
      error: () => {
        this.translate.get('event.message.update.failed').subscribe(msg => this.toast.error(msg))
        this.saving.set(false)
      }
    })
  }
}
