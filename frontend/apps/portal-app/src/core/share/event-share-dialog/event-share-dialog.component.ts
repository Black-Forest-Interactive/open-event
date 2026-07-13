import { Component, computed, inject, resource, ChangeDetectionStrategy } from '@angular/core'
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog'
import { MatIcon } from '@angular/material/icon'
import { MatIconButton } from '@angular/material/button'
import { TranslatePipe } from '@ngx-translate/core'
import { LoadingBarComponent, toPromise } from '@open-event/shared'
import { EventService } from '@open-event/portal'
import { ShareDetailsComponent } from '../share-details/share-details.component'
import { EventShareSheetData } from '../event-share-sheet/event-share-sheet.component'

@Component({
  selector: 'portal-event-share-dialog',
  templateUrl: './event-share-dialog.component.html',
  imports: [ShareDetailsComponent, MatIcon, MatIconButton, TranslatePipe, LoadingBarComponent],
  changeDetection: ChangeDetectionStrategy.Eager,
  standalone: true
})
export class EventShareDialogComponent {
  private data = inject<EventShareSheetData>(MAT_DIALOG_DATA)
  private dialogRef = inject(MatDialogRef)
  private eventService = inject(EventService)

  readonly eventTitle = this.data.eventTitle

  private infoResource = resource({
    loader: (p) => toPromise(this.eventService.getEventInfo(this.data.eventId), p.abortSignal)
  })

  readonly info = computed(() => this.infoResource.value())
  readonly reloading = this.infoResource.isLoading

  close() {
    this.dialogRef.close()
  }
}
