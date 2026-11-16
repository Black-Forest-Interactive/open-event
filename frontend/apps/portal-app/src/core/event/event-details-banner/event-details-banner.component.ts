import { Component, computed, effect, inject, input, output, signal, ChangeDetectionStrategy } from '@angular/core'
import { RouterLink } from '@angular/router'
import { MatTooltipModule } from '@angular/material/tooltip'
import { MatIconModule } from '@angular/material/icon'
import { EventInfo } from '@open-event/core'

import { MatProgressSpinnerModule } from '@angular/material/progress-spinner'
import { ImageUploadService } from '@open-event/portal'
import { MatButtonModule } from '@angular/material/button'
import { MatProgressBarModule } from '@angular/material/progress-bar'
import { HotToastService } from '@ngxpert/hot-toast'
import { TranslatePipe, TranslateService } from '@ngx-translate/core'
import { CategoryChipComponent, getCategoryStyle } from '@open-event/ui'

@Component({
  selector: 'portal-event-details-banner',
  imports: [MatButtonModule, MatIconModule, MatTooltipModule, MatProgressBarModule, MatProgressSpinnerModule, TranslatePipe, CategoryChipComponent, RouterLink],
  templateUrl: './event-details-banner.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './event-details-banner.component.scss'
})
export class EventDetailsBannerComponent {
  data = input<EventInfo>()
  bookmarked = input<boolean>(false)
  toggleBookmark = output<void>()
  share = output<void>()
  readonly canEdit = computed(() => this.data()?.canEdit ?? false)
  readonly readOnly = computed(() => {
    const status = this.data()?.event.status
    return status === 'ENDED' || status === 'CANCELED'
  })
  readonly isBookmarked = computed(() => this.bookmarked())
  readonly categories = computed(() => this.data()?.categories ?? [])
  readonly audiences = computed(() => this.data()?.audiences ?? [])
  readonly featured = computed(() => this.data()?.event.featured ?? false)
  readonly canceled = computed(() => this.data()?.event.status === 'CANCELED')
  readonly ended = computed(() => this.data()?.event.status === 'ENDED')
  readonly mediaStyle = computed(() => getCategoryStyle(this.categories()[0]?.name ?? ''))
  private service = inject(ImageUploadService)
  readonly isUploading = computed(() => this.service.isUploading())
  readonly uploadPercentage = computed(() => this.service.uploadPercentage())
  private toast = inject(HotToastService)
  private translate = inject(TranslateService)
  private bannerImage = signal('')
  readonly bannerUrl = computed(() => this.bannerImage())
  readonly hasImage = signal(true)

  constructor() {
    effect(() => {
      const data = this.data()
      if (data) {
        this.hasImage.set(true)
        this.bannerImage.set(this.service.getBannerImageUrl(data.event.id))
      }
    })
  }

  onImageSelected(event: Event) {
    const eventId = this.data()?.event.id
    const target = event.target as HTMLInputElement
    const file = target.files?.[0]
    if (!file || !eventId) return

    const validation = this.service.validateImage(file)
    if (!validation.valid) {
      this.toast.error()
      return
    }

    const reader = new FileReader()
    reader.onload = (e) => {
      this.hasImage.set(true)
      this.bannerImage.set(e.target?.result as string)
    }
    reader.readAsDataURL(file)

    this.service.uploadBannerImage(eventId, file).subscribe({
      next: (response) => {
        if (response.success && response.imageUrl) {
          this.hasImage.set(true)
          this.bannerImage.set(response.imageUrl)
          this.translate.get('event.message.bannerUpdated').subscribe((t) => this.toast.success(t))
        }
      },
      error: () => {
        this.toast.error()
        this.hasImage.set(false)
      }
    })

    target.value = ''
  }

  onImageError() {
    this.hasImage.set(false)
  }
}
