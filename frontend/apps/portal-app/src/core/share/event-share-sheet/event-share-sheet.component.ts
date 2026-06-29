import { Component, computed, inject, resource } from '@angular/core'
import { MAT_BOTTOM_SHEET_DATA, MatBottomSheetRef } from '@angular/material/bottom-sheet'
import { MatIcon } from '@angular/material/icon'
import { MatIconButton } from '@angular/material/button'
import { TranslatePipe } from '@ngx-translate/core'
import { LoadingBarComponent, toPromise } from '@open-event/shared'
import { EventService } from '@open-event/portal'
import { ShareDetailsComponent } from '../share-details/share-details.component'

export interface EventShareSheetData {
  eventId: number
  eventTitle: string
}

@Component({
  selector: 'portal-event-share-sheet',
  templateUrl: './event-share-sheet.component.html',
  imports: [ShareDetailsComponent, MatIcon, MatIconButton, TranslatePipe, LoadingBarComponent],
  standalone: true
})
export class EventShareSheetComponent {
  private data = inject<EventShareSheetData>(MAT_BOTTOM_SHEET_DATA)
  private sheetRef = inject(MatBottomSheetRef)
  private eventService = inject(EventService)

  readonly eventTitle = this.data.eventTitle

  private infoResource = resource({
    loader: (p) => toPromise(this.eventService.getEventInfo(this.data.eventId), p.abortSignal)
  })

  readonly info = computed(() => this.infoResource.value())
  readonly reloading = this.infoResource.isLoading

  onSharingChanged(enabled: boolean) {
    this.eventService.setShared(this.data.eventId, enabled).subscribe((updated) => {
      this.infoResource.set(updated)
    })
  }

  close() {
    this.sheetRef.dismiss()
  }
}
