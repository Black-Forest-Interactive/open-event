package de.sambalmueslie.openevent.core.logic.export

import de.sambalmueslie.openevent.core.model.EventInfo
import io.micronaut.http.server.types.files.SystemFile

interface EventExporter {
    fun exportEvents(provider: () -> Sequence<EventInfo>): SystemFile?
    fun exportEvent(info: EventInfo): SystemFile?
}
