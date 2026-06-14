import { Component, computed, input } from '@angular/core'
import { EventInfo } from '@open-event/core'
import { DatePipe } from '@angular/common'
import { MatIcon } from '@angular/material/icon'
import { TranslatePipe } from '@ngx-translate/core'

@Component({
  selector: 'portal-event-fact-row',
  templateUrl: './event-fact-row.component.html',
  styleUrl: './event-fact-row.component.scss',
  imports: [DatePipe, MatIcon, TranslatePipe],
  standalone: true
})
export class EventFactRowComponent {
  info = input.required<EventInfo>()

  readonly start = computed(() => this.info().event.start)
  readonly finish = computed(() => this.info().event.finish)
  readonly hasLocation = computed(() => this.info().event.hasLocation)
  readonly location = computed(() => this.info().location)

  readonly durationMinutes = computed(() => {
    const start = new Date(this.start()).getTime()
    const finish = new Date(this.finish()).getTime()
    return Math.max(0, Math.round((finish - start) / 60000))
  })
}
