import { Component, computed, input, output } from '@angular/core'
import { LoadingBarComponent } from '@open-event/shared'
import { EventInfo } from '@open-event/core'
import { TranslatePipe } from '@ngx-translate/core'
import { ShareButtons } from 'ngx-sharebuttons/buttons'
import { MatButtonModule } from '@angular/material/button'
import { MatDivider } from '@angular/material/divider'
import { MatIcon } from '@angular/material/icon'
import { MatTabGroup, MatTab } from '@angular/material/tabs'
import { DatePipe } from '@angular/common'

@Component({
  selector: 'portal-share-details',
  templateUrl: './share-details.component.html',
  styleUrl: './share-details.component.scss',
  imports: [TranslatePipe, ShareButtons, LoadingBarComponent, MatButtonModule, MatDivider, MatIcon, MatTabGroup, MatTab, DatePipe],
  standalone: true
})
export class ShareDetailsComponent {
  info = input.required<EventInfo>()
  reloading = input(false)
  readonly share = computed(() => this.info().share)
  readonly active = computed(() => this.share() && this.share()?.share?.enabled)
  readonly title = computed(() => this.info().event.title)
  readonly start = computed(() => this.info().event.start)
  readonly url = computed(() => this.share()?.url ?? '')
  readonly qrUrl = computed(() => `https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=${encodeURIComponent(this.url())}`)

  changed = output<boolean>()

  enableSharing() {
    this.changed.emit(true)
  }

  disableSharing() {
    this.changed.emit(false)
  }

  print() {
    window.print()
  }
}
