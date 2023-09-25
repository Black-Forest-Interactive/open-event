package de.sambalmueslie.openevent.core.model

import de.sambalmueslie.openevent.core.BusinessObjectChangeRequest
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class HistoryEntryChangeRequest(
    val type: HistoryEntryType,
    val message: String,
    val source: HistoryEntrySource,
    val info: String
) : BusinessObjectChangeRequest
