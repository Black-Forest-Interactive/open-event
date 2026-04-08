import { Component, computed, effect, inject, input, signal } from '@angular/core'
import { MatTooltipModule } from '@angular/material/tooltip'
import { MatIconModule } from '@angular/material/icon'
import { EventInfo } from '@open-event/core'

import { MatProgressSpinnerModule } from '@angular/material/progress-spinner'
import { ImageUploadService } from '@open-event/portal'
import { MatButtonModule } from '@angular/material/button'
import { MatProgressBarModule } from '@angular/material/progress-bar'
import { HotToastService } from '@ngxpert/hot-toast'
import { TranslatePipe, TranslateService } from '@ngx-translate/core'

@Component({
  selector: 'portal-event-details-banner',
  imports: [MatButtonModule, MatIconModule, MatTooltipModule, MatProgressBarModule, MatProgressSpinnerModule, TranslatePipe],
  templateUrl: './event-details-banner.component.html',
  styleUrl: './event-details-banner.component.scss'
})
export class EventDetailsBannerComponent {
  data = input<EventInfo>()
  readonly canEdit = computed(() => this.data()?.canEdit ?? false)
  private service = inject(ImageUploadService)
  readonly isUploading = computed(() => this.service.isUploading())
  readonly uploadPercentage = computed(() => this.service.uploadPercentage())
  private toast = inject(HotToastService)
  private translate = inject(TranslateService)
  private defaultBannerImage = '/img/banner.jpg'
  private bannerImage = signal(this.defaultBannerImage)
  readonly bannerUrl = computed(() => this.bannerImage())

  constructor() {
    effect(() => {
      const data = this.data()
      if (data) this.bannerImage.set(this.service.getBannerImageUrl(data.event.id))
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
    reader.onload = (e) => this.bannerImage.set(e.target?.result as string)
    reader.readAsDataURL(file)

    this.service.uploadBannerImage(eventId, file).subscribe({
      next: (response) => {
        if (response.success && response.imageUrl) {
          this.bannerImage.set(response.imageUrl)
          this.translate.get('event.message.bannerUpdated').subscribe((t) => this.toast.success(t))
        }
      },
      error: () => {
        this.toast.error()
        this.bannerImage.set(this.defaultBannerImage)
      }
    })

    target.value = ''
  }

  onImageError() {
    this.bannerImage.set(this.defaultBannerImage)
  }
}
