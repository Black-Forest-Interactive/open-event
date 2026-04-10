import { Component, computed, inject, resource } from '@angular/core'

import { toPromise } from '@open-event/shared'
import { SettingsService } from '@open-event/portal'
import { DomSanitizer } from '@angular/platform-browser'
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
  private sanitizer = inject(DomSanitizer)

  private termsResource = resource({
    loader: (param) => toPromise(this.service.getTerms(), param.abortSignal)
  })

  readonly terms = computed(() => this.sanitizer.bypassSecurityTrustHtml(this.termsResource.value()?.text ?? ''))
}
