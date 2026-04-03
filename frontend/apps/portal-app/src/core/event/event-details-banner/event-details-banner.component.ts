import { Component, computed, effect, inject, input, signal } from '@angular/core'
import { MatTooltipModule } from '@angular/material/tooltip'
import { MatIconModule } from '@angular/material/icon'
import { EventInfo } from '@open-event/core'

import { MatProgressSpinnerModule } from '@angular/material/progress-spinner'
import { ImageUploadService } from '@open-event/portal'
import { MatButtonModule } from '@angular/material/button'
import { MatProgressBarModule } from '@angular/material/progress-bar'
import { HotToastService } from '@ngxpert/hot-toast'

@Component({
  selector: 'portal-event-details-banner',
  imports: [MatButtonModule, MatIconModule, MatTooltipModule, MatProgressBarModule, MatProgressSpinnerModule],
  templateUrl: './event-details-banner.component.html',
  styleUrl: './event-details-banner.component.scss'
})
export class EventDetailsBannerComponent {
  protected service = inject(ImageUploadService)
  private toast = inject(HotToastService)

  data = input<EventInfo>()

  private defaultBannerImage = '/img/banner.jpg'
  bannerImage = signal(this.defaultBannerImage)

  readonly canEdit = computed(() => this.data()?.canEdit ?? false)

  constructor() {
    effect(() => {
      const data = this.data()
      if (data) {
        const url = this.service.getBannerImageUrl(data.event.id)
        this.bannerImage.set(url)
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
      this.bannerImage.set(e.target?.result as string)
    }
    reader.readAsDataURL(file)

    this.service.uploadBannerImage(eventId, file).subscribe({
      next: (response) => {
        if (response.success && response.imageUrl) {
          this.bannerImage.set(response.imageUrl)
          this.toast.info('Banner updated successfully!')
        }
      },
      error: (error) => {
        console.error(error)
        this.toast.error()
        this.bannerImage.set(this.defaultBannerImage)
      }
    })

    // Clear the input
    target.value = ''
  }

  onImageError() {
    this.bannerImage.set(this.defaultBannerImage)
  }
}
