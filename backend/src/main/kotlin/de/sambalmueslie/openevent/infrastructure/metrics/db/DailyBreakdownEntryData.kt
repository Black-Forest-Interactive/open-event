package de.sambalmueslie.openevent.infrastructure.metrics.db

import de.sambalmueslie.openevent.infrastructure.metrics.api.DailyBreakdownEntry
import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable
import java.time.LocalDate

@Introspected
@Serdeable.Serializable
@Serdeable.Deserializable
data class DailyBreakdownEntryData(
    val day: LocalDate,
    var entries: List<MetricsEntryData> = emptyList()
) {
    fun convert(): DailyBreakdownEntry {
        return DailyBreakdownEntry(day, entries.map { it.convert() })
    }
}
