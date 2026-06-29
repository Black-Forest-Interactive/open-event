import { Component, computed, inject, resource, signal } from '@angular/core'
import { MAT_BOTTOM_SHEET_DATA, MatBottomSheetRef } from '@angular/material/bottom-sheet'
import { FormsModule } from '@angular/forms'
import { DatePipe } from '@angular/common'
import { MatButton, MatIconButton } from '@angular/material/button'
import { MatChipsModule } from '@angular/material/chips'
import { MatFormField, MatLabel } from '@angular/material/form-field'
import { MatInput } from '@angular/material/input'
import { MatIcon } from '@angular/material/icon'
import { MatDivider } from '@angular/material/divider'
import { TranslatePipe, TranslateService } from '@ngx-translate/core'
import { HotToastService } from '@ngxpert/hot-toast'
import { LoadingBarComponent, toPromise } from '@open-event/shared'
import { Announcement, AnnouncementChangeRequest, AnnouncementService } from '@open-event/portal'

export interface EventBroadcastSheetData {
  eventId: number
  eventTitle: string
  participantCount: number
  defaultSubject?: string
  defaultBody?: string
}

@Component({
  selector: 'portal-event-broadcast-sheet',
  templateUrl: './event-broadcast-sheet.component.html',
  imports: [FormsModule, DatePipe, MatButton, MatIconButton, MatChipsModule, MatFormField, MatLabel, MatInput, MatIcon, MatDivider, TranslatePipe, LoadingBarComponent],
  standalone: true
})
export class EventBroadcastSheetComponent {
  private data = inject<EventBroadcastSheetData>(MAT_BOTTOM_SHEET_DATA)
  private bottomSheetRef = inject(MatBottomSheetRef)
  private service = inject(AnnouncementService)
  private toast = inject(HotToastService)
  private translateService = inject(TranslateService)

  readonly eventId = this.data.eventId
  readonly eventTitle = this.data.eventTitle
  readonly participantCount = this.data.participantCount

  readonly subject = signal(this.data.defaultSubject ?? '')
  readonly body = signal(this.data.defaultBody ?? '')
  readonly sending = signal(false)

  private historyResource = resource({
    loader: (p) => toPromise(this.service.getAnnouncements(this.eventId, 0, 20), p.abortSignal)
  })
  readonly history = computed(() => this.historyResource.value()?.content ?? [])
  readonly historyLoading = this.historyResource.isLoading

  readonly templates = [
    { key: 'event.broadcast.template.reminder', subject: '', body: '' },
    { key: 'event.broadcast.template.bring', subject: '', body: '' },
    { key: 'event.broadcast.template.thanks', subject: '', body: '' },
  ]

  applyTemplate(key: string) {
    this.translateService.get(key).subscribe((t: string) => this.subject.set(t))
  }

  send() {
    if (this.sending() || !this.subject().trim()) return
    this.sending.set(true)
    this.service.createAnnouncement(this.eventId, new AnnouncementChangeRequest(this.subject(), this.body())).subscribe({
      next: () => {
        this.sending.set(false)
        this.subject.set('')
        this.body.set('')
        this.historyResource.reload()
        this.translateService.get('event.broadcast.message.sent').subscribe((t) => this.toast.success(t))
      },
      error: () => {
        this.sending.set(false)
        this.translateService.get('action.error').subscribe((t) => this.toast.error(t))
      }
    })
  }

  close() {
    this.bottomSheetRef.dismiss()
  }

  trackById(_: number, item: Announcement) {
    return item.id
  }
}
