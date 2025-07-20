package de.sambalmueslie.openevent.infrastructure.metrics

import de.sambalmueslie.openevent.infrastructure.metrics.db.MetricsData
import java.time.LocalDate
import java.util.concurrent.ConcurrentHashMap

class ResourceMetricsCollectionEntry(
    val id: String,
    val source: String,
    val action: String,
    val resource: Long,
    val timestamp: LocalDate,
) {

    @Volatile
    private var entries = ConcurrentHashMap<String, Int>()

    fun add(accountExternalId: String): Int {
        entries.merge(accountExternalId, 1, Int::plus)
        return entries.size
    }


    fun flush(): MetricsData? {
        if (entries.isEmpty()) return null
        val snapshot = entries
        entries = ConcurrentHashMap<String, Int>()

        val totalCount = snapshot.values.sum()
        val uniqueCount = snapshot.keys.size
        return MetricsData(id, source, resource, action, timestamp, totalCount, uniqueCount)
    }


}
