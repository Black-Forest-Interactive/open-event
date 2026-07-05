import { Component, computed, inject, resource } from '@angular/core'

import { toPromise } from '@open-event/shared'
import { SettingsService } from '@open-event/portal'
import { MatCard } from '@angular/material/card'
import { MatIcon } from '@angular/material/icon'
import { TranslatePipe } from '@ngx-translate/core'

@Component({
  selector: 'portal-imprint',
  imports: [MatCard, MatIcon, TranslatePipe],
  templateUrl: './imprint.component.html',
  styleUrl: './imprint.component.scss'
})
export class ImprintComponent {
  private service = inject(SettingsService)

  private termsResource = resource({
    loader: (param) => toPromise(this.service.getTerms(), param.abortSignal)
  })

  // Bind the raw text via [innerHtml] so Angular's built-in sanitizer strips any script/event-handler
  // content (the terms text is not sanitized server-side). Do NOT reintroduce bypassSecurityTrustHtml here.
  readonly terms = computed(() => this.termsResource.value()?.text ?? '')
}
