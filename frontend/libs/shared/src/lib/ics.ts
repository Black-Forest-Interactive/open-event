import { DateTime } from 'luxon'

export interface IcsEvent {
  title: string
  longText: string
  start: string
  finish: string
  location?: {
    street: string
    streetNumber: string
    zip: string
    city: string
  }
}

export function downloadIcs(event: IcsEvent) {
  const start = DateTime.fromISO(event.start).toUTC().toFormat("yyyyMMdd'T'HHmmss'Z'")
  const finish = DateTime.fromISO(event.finish).toUTC().toFormat("yyyyMMdd'T'HHmmss'Z'")
  const location = event.location
  const locationLine = location ? `LOCATION:${location.street} ${location.streetNumber}, ${location.zip} ${location.city}\n` : ''

  const icsContent = `
BEGIN:VCALENDAR
VERSION:2.0
BEGIN:VEVENT
SUMMARY:${event.title}
${locationLine}DTSTART:${start}
DTEND:${finish}
DESCRIPTION:${event.longText}
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
