import { Component, computed, inject, input, output, signal, ChangeDetectionStrategy } from '@angular/core'
import { EventInfo } from '@open-event/core'
import { EventService, ExportService } from '@open-event/admin'
import { download } from '@open-event/shared'
import { MatDialog } from '@angular/material/dialog'
import { Router } from '@angular/router'
import { HotToastService } from '@ngxpert/hot-toast'
import { TranslatePipe, TranslateService } from '@ngx-translate/core'
import { MatButton } from '@angular/material/button'
import { MatIcon } from '@angular/material/icon'
import { MatSlideToggle } from '@angular/material/slide-toggle'
import { MatProgressSpinner } from '@angular/material/progress-spinner'
import { MatCard } from '@angular/material/card'
import { EventChangeDialogComponent } from '../event-change-dialog/event-change-dialog.component'
import { EventDeleteDialogComponent } from '../event-delete-dialog/event-delete-dialog.component'
import { EventCancelDialogComponent } from '../event-cancel-dialog/event-cancel-dialog.component'

const STATUS_STYLE: Record<string, { icon: string; badgeClass: string }> = {
  DRAFT: { icon: 'edit_note', badgeClass: 'bg-blue-100 text-blue-700' },
  ACTIVE: { icon: 'check_circle', badgeClass: 'bg-green-100 text-green-700' },
  CANCELED: { icon: 'cancel', badgeClass: 'bg-red-100 text-red-700' },
  ENDED: { icon: 'event_busy', badgeClass: 'bg-gray-200 text-gray-700' }
}

@Component({
  selector: 'admin-event-details-actions',
  imports: [TranslatePipe, MatButton, MatIcon, MatSlideToggle, MatProgressSpinner, MatCard],
  templateUrl: './event-details-actions.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './event-details-actions.component.scss'
})
export class EventDetailsActionsComponent {
  event = input.required<EventInfo>()
  changed = output()

  private service = inject(EventService)
  private exportService = inject(ExportService)
  private dialog = inject(MatDialog)
  private router = inject(Router)
  private toast = inject(HotToastService)
  private translation = inject(TranslateService)

  featuring = signal(false)
  exporting = signal(false)
  copied = signal(false)
  private copyTimeout?: ReturnType<typeof setTimeout>

  readonly status = computed(() => this.event().event.status)
  readonly statusIcon = computed(() => STATUS_STYLE[this.status()]?.icon ?? 'help')
  readonly statusBadgeClass = computed(() => STATUS_STYLE[this.status()]?.badgeClass ?? 'bg-gray-200 text-gray-700')
  readonly cancelable = computed(() => this.status() !== 'CANCELED' && this.status() !== 'ENDED')

  readonly featured = computed(() => this.event().event.featured)
  readonly shareUrl = computed(() => this.event().share?.url ?? '')

  toggleFeatured() {
    if (this.featuring()) return
    this.featuring.set(true)
    this.service.setFeatured(this.event().event.id, !this.featured()).subscribe({
      next: () => {
        this.featuring.set(false)
        this.changed.emit()
      },
      error: () => {
        this.featuring.set(false)
        this.translation.get('event.message.error').subscribe((t) => this.toast.error(t))
      }
    })
  }

  copyShare() {
    const url = this.shareUrl()
    if (!url) return
    navigator.clipboard?.writeText(url)
    this.copied.set(true)
    clearTimeout(this.copyTimeout)
    this.copyTimeout = setTimeout(() => this.copied.set(false), 1600)
  }

  editEvent() {
    this.dialog
      .open(EventChangeDialogComponent, { data: this.event().event })
      .afterClosed()
      .subscribe((value) => {
        if (value) this.changed.emit()
      })
  }

  cancelEvent() {
    this.dialog
      .open(EventCancelDialogComponent, { width: '350px', data: this.event().event })
      .afterClosed()
      .subscribe((result) => {
        if (result) this.changed.emit()
      })
  }

  deleteEvent() {
    this.dialog
      .open(EventDeleteDialogComponent, { width: '350px', data: this.event() })
      .afterClosed()
      .subscribe((result) => {
        if (result) this.service.deleteEvent(this.event().event.id).subscribe(() => this.router.navigate(['event']))
      })
  }

  exportEvent() {
    if (this.exporting()) return
    this.exporting.set(true)
    this.exportService.exportEvent(this.event().event.id).subscribe((response) => {
      download(response)
      this.exporting.set(false)
    })
  }
}
