import { Component, inject, signal } from '@angular/core'

import { MatCard } from '@angular/material/card'
import { FeedbackChangeRequest, FeedbackFormComponent } from '@open-event/core'
import { FeedbackService } from '@open-event/portal'
import { TranslatePipe, TranslateService } from '@ngx-translate/core'
import { MatDivider } from '@angular/material/divider'
import { LoadingBarComponent } from '@open-event/shared'
import { HotToastService } from '@ngxpert/hot-toast'

@Component({
  selector: 'app-feedback',
  imports: [MatCard, FeedbackFormComponent, TranslatePipe, MatDivider, LoadingBarComponent],
  templateUrl: './feedback.component.html',
  styleUrl: './feedback.component.scss'
})
export class FeedbackComponent {
  private service = inject(FeedbackService)
  private toast = inject(HotToastService)
  private translate = inject(TranslateService)

  loading = signal(false)

  submit(request: FeedbackChangeRequest) {
    if (this.loading()) return
    this.loading.set(true)
    this.service.createFeedback(request).subscribe({
      next: () => this.toast.success(this.translate.instant('feedback.confirmation')),
      error: () => this.toast.error(),
      complete: () => this.loading.set(false)
    })
  }
}
