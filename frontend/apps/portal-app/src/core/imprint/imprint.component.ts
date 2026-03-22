import { Component, computed, inject, resource } from '@angular/core'

import { toPromise } from '@open-event/shared'
import { SettingsService } from '@open-event/portal'
import { DomSanitizer } from '@angular/platform-browser'
import { MatCard } from '@angular/material/card'
import { TranslatePipe } from '@ngx-translate/core'
import { MatDivider } from '@angular/material/divider'

@Component({
  selector: 'app-imprint',
  imports: [MatCard, TranslatePipe, MatDivider],
  templateUrl: './imprint.component.html',
  styleUrl: './imprint.component.scss'
})
export class ImprintComponent {
  private service = inject(SettingsService)
  private sanitizer = inject(DomSanitizer)

  termsResource = resource({
    loader: (param) => {
      return toPromise(this.service.getTerms())
    }
  })

  terms = computed(() => this.sanitizer.bypassSecurityTrustHtml(this.termsResource.value()?.text ?? ''))
}
