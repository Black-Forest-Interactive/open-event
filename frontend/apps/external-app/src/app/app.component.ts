import { Component, computed, inject, resource, signal } from '@angular/core'
import { RouterModule } from '@angular/router'
import { TranslateService, TranslatePipe } from '@ngx-translate/core'
import { toPromise } from '@open-event/shared'
import { SettingsService } from '@open-event/external'
import { AppToolbarComponent, AppFooterComponent } from '@open-event/ui'
import { MatIconButton } from '@angular/material/button'
import { MatIcon } from '@angular/material/icon'
import { MatMenu, MatMenuItem, MatMenuTrigger } from '@angular/material/menu'

@Component({
  imports: [RouterModule, AppToolbarComponent, AppFooterComponent, MatIconButton, MatIcon, MatMenu, MatMenuItem, MatMenuTrigger, TranslatePipe],
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent {
  private translate = inject(TranslateService)
  private settings = inject(SettingsService)

  readonly currentLang = signal(this.translate.currentLang ?? this.translate.defaultLang)

  private titleResource = resource({
    loader: (p) => toPromise(this.settings.getTitle(), p.abortSignal)
  })

  private portalUrlResource = resource({
    loader: (p) => toPromise(this.settings.getPortalUrl(), p.abortSignal)
  })

  readonly title = computed(() => this.titleResource.value()?.text ?? '')
  readonly portalUrl = computed(() => this.portalUrlResource.value()?.url ?? '')

  setLang(lang: string) {
    this.translate.use(lang)
    this.currentLang.set(lang)
  }
}
