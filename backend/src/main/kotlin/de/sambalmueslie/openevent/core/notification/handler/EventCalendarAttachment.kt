package de.sambalmueslie.openevent.core.notification.handler

import biweekly.Biweekly
import biweekly.ICalendar
import biweekly.component.VEvent
import biweekly.property.Summary
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.location.api.Location
import de.sambalmueslie.openevent.infrastructure.mail.api.Attachment
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

object EventCalendarAttachment {
    private val ZONE_OFFSET = ZoneId.of("Europe/Berlin")

    fun create(event: Event, location: Location? = null): Attachment {
        val cal = ICalendar()
        val e = VEvent()
        e.summary = Summary(event.title)
        e.setDateStart(convert(event.start), true)
        e.setDateEnd(convert(event.finish), true)
        location?.let { e.setLocation(it.format()) }
        cal.addEvent(e)

        val content = Biweekly.write(cal).go().toByteArray(StandardCharsets.UTF_8)
        return Attachment("invitation.ics", content, "text/calendar")
    }

    private fun convert(timestamp: LocalDateTime): Date {
        val offset = ZONE_OFFSET.rules.getOffset(timestamp)
        return Date.from(timestamp.toInstant(offset))
    }
}
