import { Component, computed, effect, inject, input, output, resource, signal, ChangeDetectionStrategy } from '@angular/core'
import { DomSanitizer } from '@angular/platform-browser'
import { Clipboard } from '@angular/cdk/clipboard'
import { LoadingBarComponent, download, toPromise } from '@open-event/shared'
import { EventInfo } from '@open-event/core'
import { EventService } from '@open-event/portal'
import { TranslatePipe, TranslateService } from '@ngx-translate/core'
import { HotToastService } from '@ngxpert/hot-toast'
import { MatButtonModule } from '@angular/material/button'
import { MatButtonToggleModule } from '@angular/material/button-toggle'
import { MatIcon } from '@angular/material/icon'

@Component({
  selector: 'portal-share-details',
  templateUrl: './share-details.component.html',
  imports: [TranslatePipe, LoadingBarComponent, MatButtonModule, MatButtonToggleModule, MatIcon],
  changeDetection: ChangeDetectionStrategy.Eager,
  standalone: true
})
export class ShareDetailsComponent {
  private eventService = inject(EventService)
  private clipboard = inject(Clipboard)
  private sanitizer = inject(DomSanitizer)
  private toast = inject(HotToastService)
  private translateService = inject(TranslateService)

  info = input.required<EventInfo>()
  reloading = input(false)

  readonly activeTab = signal<'link' | 'poster'>('link')
  private posterTabOpened = signal(false)

  readonly share = computed(() => this.info().share)
  readonly active = computed(() => !!this.share()?.share?.enabled)
  readonly title = computed(() => this.info().event.title)
  readonly url = computed(() => this.share()?.url ?? '')
  readonly qrUrl = computed(() => `https://api.qrserver.com/v1/create-qr-code/?size=300x300&margin=0&qzone=1&data=${encodeURIComponent(this.url())}`)

  readonly whatsappHref = computed(() => `https://wa.me/?text=${encodeURIComponent(this.title() + ' – ' + this.url())}`)
  readonly mailHref = computed(() => `mailto:?subject=${encodeURIComponent(this.title())}&body=${encodeURIComponent(this.url())}`)

  private posterEventId = computed(() => (this.posterTabOpened() && this.active() ? this.info().event.id : undefined))
  private posterResource = resource({
    params: this.posterEventId,
    loader: (p) => (p.params ? toPromise(this.eventService.exportEvent(p.params), p.abortSignal) : Promise.resolve(undefined))
  })
  readonly posterLoading = this.posterResource.isLoading
  readonly posterError = this.posterResource.error

  readonly posterObjectUrl = signal<string | undefined>(undefined)
  readonly posterPreviewUrl = computed(() => {
    const objectUrl = this.posterObjectUrl()
    return objectUrl ? this.sanitizer.bypassSecurityTrustResourceUrl(objectUrl) : undefined
  })

  closed = output<void>()

  constructor() {
    effect((onCleanup) => {
      const blob = this.posterResource.value()?.body
      const objectUrl = blob ? URL.createObjectURL(blob) : undefined
      this.posterObjectUrl.set(objectUrl)
      onCleanup(() => {
        if (objectUrl) URL.revokeObjectURL(objectUrl)
      })
    })
  }

  selectTab(tab: 'link' | 'poster') {
    this.activeTab.set(tab)
    if (tab === 'poster') this.posterTabOpened.set(true)
  }

  copyLink() {
    if (!this.url()) return
    this.clipboard.copy(this.url())
    this.translateService.get('event.message.linkCopied').subscribe((t) => this.toast.success(t))
  }

  reloadPoster() {
    this.posterResource.reload()
  }

  downloadPoster() {
    const response = this.posterResource.value()
    if (response) download(response)
  }
}
