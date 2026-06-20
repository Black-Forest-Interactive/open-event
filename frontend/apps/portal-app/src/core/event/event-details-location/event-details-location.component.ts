import { Component, input } from '@angular/core'
import { EventInfo } from '@open-event/core'
import { LocationMapComponent } from '@open-event/ui'
import { TranslatePipe } from '@ngx-translate/core'
import { MatButtonModule } from '@angular/material/button'
import { MatIcon } from '@angular/material/icon'
import { MatDivider } from '@angular/material/divider'
import { downloadIcs } from '@open-event/shared'

@Component({
  selector: 'portal-event-details-location',
  templateUrl: './event-details-location.component.html',
  styleUrl: './event-details-location.component.scss',
  imports: [LocationMapComponent, TranslatePipe, MatButtonModule, MatIcon, MatDivider],
  standalone: true
})
export class EventDetailsLocationComponent {
  info = input.required<EventInfo>()

  openRoute() {
    const location = this.info().location
    if (!location) return

    const destination = `${location.street} ${location.streetNumber}, ${location.zip} ${location.city}`
    window.open(`https://www.google.com/maps/dir/?api=1&destination=${destination}`, '_blank')
  }

  downloadICS() {
    const info = this.info()
    if (!info.location) return

    downloadIcs({ title: info.event.title, longText: info.event.longText, start: info.event.start, finish: info.event.finish, location: info.location })
  }
}
