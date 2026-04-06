import { Component, computed, signal } from '@angular/core'
import { AccountDisplayNamePipe, EventSearchEntry } from '@open-event/core'
import { DatePipe } from '@angular/common'
import { MatIcon } from '@angular/material/icon'
import { TranslatePipe } from '@ngx-translate/core'
import { MatButton } from '@angular/material/button'
import { Subject } from 'rxjs'

@Component({
  selector: 'portal-event-board-map-popup',
  templateUrl: './event-board-map-popup.component.html',
  styleUrl: './event-board-map-popup.component.scss',
  imports: [AccountDisplayNamePipe, DatePipe, MatIcon, TranslatePipe, MatButton],
  standalone: true
})
export class EventBoardMapPopupComponent {
  data = signal<EventSearchEntry | undefined>(undefined)
  close = new Subject<boolean>()

  readonly title = computed(() => this.data()?.title ?? '')
  readonly owner = computed(() => this.data()?.owner)
  readonly start = computed(() => this.data()?.start ?? '')
  readonly finish = computed(() => this.data()?.finish ?? '')
  readonly hasLocation = computed(() => this.data()?.hasLocation ?? false)
  readonly street = computed(() => this.data()?.street ?? '')
  readonly streetNumber = computed(() => this.data()?.streetNumber ?? '')
  readonly zip = computed(() => this.data()?.zip ?? '')
  readonly city = computed(() => this.data()?.city ?? '')
}
