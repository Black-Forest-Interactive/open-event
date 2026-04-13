import { Component, inject, signal } from '@angular/core'

import { MatCard } from '@angular/material/card'
import { MatIcon } from '@angular/material/icon'
import { FeedbackChangeRequest } from '@open-event/core'
import { FeedbackFormComponent } from '@open-event/ui'
import { FeedbackService } from '@open-event/portal'
import { TranslatePipe, TranslateService } from '@ngx-translate/core'
import { LoadingBarComponent } from '@open-event/shared'
import { HotToastService } from '@ngxpert/hot-toast'

@Component({
  selector: 'portal-feedback',
  imports: [MatCard, MatIcon, FeedbackFormComponent, TranslatePipe, LoadingBarComponent],
  templateUrl: './feedback.component.html',
  styleUrl: './feedback.component.scss'
})
export class FeedbackComponent {
  readonly loading = signal(false)
  private service = inject(FeedbackService)
  private toast = inject(HotToastService)
  private translate = inject(TranslateService)

  submit(request: FeedbackChangeRequest) {
    if (this.loading()) return
    this.loading.set(true)
    this.service.createFeedback(request).subscribe({
      next: () => {
        this.translate.get('feedback.confirmation').subscribe((msg) => this.toast.success(msg))
        this.loading.set(false)
      },
      error: () => {
        this.toast.error()
        this.loading.set(false)
      }
    })
  }
}
