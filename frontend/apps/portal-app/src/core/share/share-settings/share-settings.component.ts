import { Component, computed, input, output, ChangeDetectionStrategy } from '@angular/core'
import { MatSlideToggle } from '@angular/material/slide-toggle'
import { MatDivider } from '@angular/material/divider'
import { TranslatePipe } from '@ngx-translate/core'
import { EventInfo } from '@open-event/core'

@Component({
  selector: 'portal-share-settings',
  templateUrl: './share-settings.component.html',
  imports: [MatSlideToggle, MatDivider, TranslatePipe],
  changeDetection: ChangeDetectionStrategy.Eager
})
export class ShareSettingsComponent {
  info = input.required<EventInfo>()
  reloading = input(false)

  readonly active = computed(() => this.info().share?.share.enabled ?? false)

  changed = output<boolean>()

  toggle() {
    this.changed.emit(!this.active())
  }
}
