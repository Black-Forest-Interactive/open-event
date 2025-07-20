package de.sambalmueslie.openevent.infrastructure.metrics

import de.sambalmueslie.openevent.common.findByIdOrNull
import de.sambalmueslie.openevent.config.AppConfig
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import de.sambalmueslie.openevent.infrastructure.metrics.api.MetricsProbe
import de.sambalmueslie.openevent.infrastructure.metrics.db.MetricsRepository
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import io.micronaut.scheduling.annotation.Scheduled
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

@Singleton
class MetricsService(
    private val repository: MetricsRepository,
    private val auditService: AuditService,
    private val timeProvider: TimeProvider,
    private val config: AppConfig
) {

    companion object {
        private val logger = LoggerFactory.getLogger(MetricsService::class.java)
    }

    fun <T : Any> getProbe(logger: String, source: KClass<T>): MetricsProbe {
        val l = auditService.getLogger(logger)
        return MetricsProbeImpl(this, timeProvider, source.simpleName ?: "unknown", l)
    }

    private val entries = ConcurrentHashMap<String, ResourceMetricsCollectionEntry>()


    fun addMetricsEntry(source: String, action: String, accountExternalId: String, resource: Long, now: LocalDateTime) {
        val timestamp = now.toLocalDate()
        val id = "$source-$action-$resource-${timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE)}"
        val entry = entries.computeIfAbsent(id) {
            ResourceMetricsCollectionEntry(id, source, action, resource, timestamp)
        }
        val size = entry.add(accountExternalId)
        checkWatermark(entry, size)
    }

    private fun checkWatermark(entry: ResourceMetricsCollectionEntry, size: Int) {
        if (size < config.metricsWatermark) return
        logger.info("[${entry.id}] watermark of ${config.metricsWatermark} is hit by $size")
        flush(entry)
    }

    @Scheduled(fixedDelay = "5m")
    fun flushToDatabase() {
        entries.values.forEach { flush(it) }
    }

    @Scheduled(cron = "0 0 3 * * *") // Every day at 3am
    fun cleanupOldMetrics() {
        val timestamp = timeProvider.now().toLocalDate().minusDays(config.metricsExpirationDays)
        val removed = repository.deleteByTimestampLessThan(timestamp)
        logger.info("Removed $removed old metrics")
    }

    private fun flush(entry: ResourceMetricsCollectionEntry) {
        val data = entry.flush() ?: return
        val existing = repository.findByIdOrNull(data.id)
        if (existing != null) {
            repository.update(existing.update(data))
        } else {
            repository.save(data)
        }
    }


}