package de.sambalmueslie.openevent.infrastructure.metrics

import de.sambalmueslie.openevent.common.findByIdOrNull
import de.sambalmueslie.openevent.config.MetricsConfig
import de.sambalmueslie.openevent.infrastructure.metrics.api.MetricsSource
import de.sambalmueslie.openevent.infrastructure.metrics.db.*
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import io.micronaut.scheduling.annotation.Scheduled
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.time.format.DateTimeFormatter
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.locks.ReentrantLock

@Singleton
class BufferedMetricsWriter(
    private val repository: MetricsTraceRepository,
    private val dailyRepository: DailyMetricsRepository,
    private val timeProvider: TimeProvider,
    private val config: MetricsConfig
) {

    companion object {
        private val logger = LoggerFactory.getLogger(BufferedMetricsWriter::class.java)
    }

    private val buffer = LinkedBlockingQueue<MetricsTraceData>()
    private val flushLock = ReentrantLock()


    internal fun add(source: MetricsSource, type: String, action: String, accountExternalId: String, resource: Long) {
        val data = MetricsTraceData(0, source, type, action, accountExternalId, resource, timeProvider.now())
        buffer.add(data)
        checkWatermark()
    }


    private fun checkWatermark() {
        val size = buffer.size
        if (size < config.watermark) return
        logger.info("Watermark of ${config.watermark} is hit by $size")
        flush()
    }


    @Scheduled(fixedDelay = "30s")
    fun flush() {
        if (!flushLock.tryLock()) return
        try {
            if (buffer.isEmpty()) return
            val batch = mutableListOf<MetricsTraceData>()
            buffer.drainTo(batch)
            if (batch.isNotEmpty()) {
                writeToDb(batch)
            }
        } finally {
            flushLock.unlock()
        }
    }

    private fun writeToDb(data: List<MetricsTraceData>) {
        repository.saveAll(data)
        data.groupBy { dailyBucketId(it) }.forEach { (id, rows) -> upsertDaily(id, rows) }
    }

    private fun dailyBucketId(d: MetricsTraceData) = "${d.type}-${d.action}-${d.resource}-${d.timestamp.toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE)}"

    private fun upsertDaily(id: String, rows: List<MetricsTraceData>) {
        val existing = dailyRepository.findByIdOrNull(id)
        val first = rows.first()
        val result = existing ?: DailyMetricsData(id, first.type, first.resource, first.action, first.timestamp.toLocalDate(), 0, 0, mutableListOf())

        rows.groupBy { it.source }.forEach { (source, sourceRows) ->
            val total = sourceRows.size
            val unique = sourceRows.map { it.accountExternalId }.toSet().size
            result.totalCount += total
            result.uniqueCount += unique

            val entry = result.entries.find { it.source == source }
            if (entry != null) {
                entry.totalCount += total
                entry.uniqueCount += unique
            } else {
                result.entries.add(MetricsEntryData(source, total, unique))
            }
        }

        if (existing != null) dailyRepository.update(result) else dailyRepository.save(result)
    }

}
