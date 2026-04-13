import { Component, input } from '@angular/core'
import { EventInfo } from '@open-event/core'
import { LocationMapComponent } from '@open-event/ui'
import { TranslatePipe } from '@ngx-translate/core'
import { MatButtonModule } from '@angular/material/button'
import { MatIcon } from '@angular/material/icon'
import { MatDivider } from '@angular/material/divider'
import { DateTime } from 'luxon'

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
    const location = this.info().location
    if (!location) return

    const start = DateTime.fromISO(info.event.start).toUTC().toFormat("yyyyMMdd'T'HHmmss'Z'")
    const finish = DateTime.fromISO(info.event.finish).toUTC().toFormat("yyyyMMdd'T'HHmmss'Z'")

    const icsContent = `
BEGIN:VCALENDAR
VERSION:2.0
BEGIN:VEVENT
SUMMARY:${info.event.title}
LOCATION:${location.street} ${location.streetNumber}, ${location.zip} ${location.city}
DTSTART:${start}
DTEND:${finish}
DESCRIPTION:${info.event.longText}
END:VEVENT
END:VCALENDAR
  `.trim()

    const blob = new Blob([icsContent], { type: 'text/calendar' })
    const url = URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = 'veranstaltung.ics'
    a.click()
    URL.revokeObjectURL(url)
  }
}
