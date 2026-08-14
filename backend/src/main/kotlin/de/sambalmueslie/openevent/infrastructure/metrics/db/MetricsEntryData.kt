package de.sambalmueslie.openevent.infrastructure.metrics.db

import de.sambalmueslie.openevent.infrastructure.metrics.api.MetricsEntry
import de.sambalmueslie.openevent.infrastructure.metrics.api.MetricsSource
import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable

@Introspected
@Serdeable.Serializable
@Serdeable.Deserializable
data class MetricsEntryData(
    val source: MetricsSource,
    var totalCount: Int,
    var uniqueCount: Int,
) {
    fun convert(): MetricsEntry {
        return MetricsEntry(source, totalCount, uniqueCount)
    }
}
