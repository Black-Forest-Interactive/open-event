import { Component, inject } from '@angular/core'
import { MatButton } from '@angular/material/button'
import { TranslatePipe } from '@ngx-translate/core'
import { NewsletterStateService } from '../newsletter-state.service'

@Component({
  selector: 'admin-newsletter-status-button',
  standalone: true,
  imports: [MatButton, TranslatePipe],
  template: `
    <button matButton="outlined" (click)="state.toggle()">
      <span class="flex flex-row items-center gap-2">
        <span class="w-2 h-2 rounded-full shrink-0" [style.background-color]="state.enabled() ? '#22c55e' : 'var(--mat-sys-outline)'"></span>
        {{ (state.enabled() ? 'newsletter.enabled' : 'newsletter.disabled') | translate }}
      </span>
    </button>
  `
})
export class NewsletterStatusButtonComponent {
  readonly state = inject(NewsletterStateService)
}
