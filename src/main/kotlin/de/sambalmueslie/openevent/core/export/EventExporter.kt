package de.sambalmueslie.openevent.core.export

import de.sambalmueslie.openevent.core.event.api.EventInfo
import io.micronaut.http.server.types.files.SystemFile

interface EventExporter {
    fun exportEvents(provider: () -> Sequence<EventInfo>): SystemFile?
    fun exportEvent(info: EventInfo): SystemFile?
}
