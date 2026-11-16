import { ChangeDetectionStrategy, Component, computed, inject, resource, signal } from '@angular/core'
import { MatCard } from '@angular/material/card'
import { MatIconModule } from '@angular/material/icon'
import { MatSlideToggle, MatSlideToggleChange } from '@angular/material/slide-toggle'
import { MatTableModule } from '@angular/material/table'
import { TranslatePipe, TranslateService } from '@ngx-translate/core'
import { HotToastService } from '@ngxpert/hot-toast'
import { NotificationType, NotificationTypeService } from '@open-event/admin'
import { toPromise } from '@open-event/shared'
import { BoardComponent } from '../../shared/board/board.component'

@Component({
  selector: 'admin-notification',
  imports: [MatCard, MatIconModule, MatSlideToggle, MatTableModule, TranslatePipe, BoardComponent],
  changeDetection: ChangeDetectionStrategy.Eager,
  templateUrl: './notification.component.html'
})
export class NotificationComponent {
  private service = inject(NotificationTypeService)
  private toast = inject(HotToastService)
  private translateService = inject(TranslateService)

  private typeResource = resource({
    loader: (p) => toPromise(this.service.getTypes(0, 500), p.abortSignal)
  })

  readonly data = computed(() => [...(this.typeResource.value()?.content ?? [])].sort((a, b) => a.key.localeCompare(b.key)))
  readonly reloading = this.typeResource.isLoading
  readonly updating = signal<number | null>(null)

  displayedColumns: string[] = ['name', 'key', 'enabled']

  reload() {
    this.typeResource.reload()
  }

  toggle(type: NotificationType, change: MatSlideToggleChange) {
    this.updating.set(type.id)
    this.service.setTypeEnabled(type.id, change.checked).subscribe({
      next: (updated) => {
        this.typeResource.update((page) => (page ? { ...page, content: page.content.map((t) => (t.id === updated.id ? updated : t)) } : page))
        const key = updated.enabled ? 'notification.message.enabled' : 'notification.message.disabled'
        this.translateService.get(key, { name: updated.name }).subscribe((t) => this.toast.success(t))
        this.updating.set(null)
      },
      error: () => {
        change.source.checked = type.enabled
        this.updating.set(null)
      }
    })
  }
}
