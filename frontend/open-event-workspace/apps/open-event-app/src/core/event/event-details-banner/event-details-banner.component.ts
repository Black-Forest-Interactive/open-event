import {Component, computed, input, signal} from '@angular/core';
import {MatTooltipModule} from "@angular/material/tooltip";
import {MatIconModule} from "@angular/material/icon";
import {EventInfo} from "@open-event-workspace/core";
import {CommonModule} from "@angular/common";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {ImageUploadService} from "@open-event-workspace/app";
import {MatButtonModule} from '@angular/material/button';
import {MatProgressBarModule} from "@angular/material/progress-bar";
import {HotToastService} from "@ngxpert/hot-toast";

@Component({
  selector: 'app-event-details-banner',
  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    MatTooltipModule,
    MatProgressBarModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './event-details-banner.component.html',
  styleUrl: './event-details-banner.component.scss'
})
export class EventDetailsBannerComponent {

  data = input<EventInfo>()

  bannerImage = signal<string>('/img/banner.jpg')
  canEdit = computed(() => this.data()?.canEdit ?? false)

  constructor(protected uploadService: ImageUploadService, private toast: HotToastService) {
  }


  onImageSelected(event: Event) {
    const eventId = this.data()?.event.id
    const target = event.target as HTMLInputElement
    const file = target.files?.[0]
    if (!file || !eventId) return

    const validation = this.uploadService.validateImage(file);
    if (!validation.valid) {
      this.toast.error("Invalid file")
      return
    }

    const reader = new FileReader()
    reader.onload = (e) => {
      this.bannerImage.set(e.target?.result as string);
    };
    reader.readAsDataURL(file)

    this.uploadService.uploadBannerImage(eventId, file).subscribe({
      next: (response) => {
        if (response.success && response.imageUrl) {
          this.bannerImage.set(response.imageUrl)
          this.toast.info('Banner updated successfully!')
        }
      },
      error: (error) => {
        console.error('Upload error:', error)
        this.toast.error('Upload failed. Please try again.')
        this.bannerImage.set('/img/banner.jpg')
      }
    });

    // Clear the input
    target.value = '';
  }
}
