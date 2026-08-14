package de.sambalmueslie.openevent.infrastructure.metrics

import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import de.sambalmueslie.openevent.infrastructure.metrics.api.DailyMetrics
import de.sambalmueslie.openevent.infrastructure.metrics.api.MetricsProbe
import de.sambalmueslie.openevent.infrastructure.metrics.api.MetricsSource
import de.sambalmueslie.openevent.infrastructure.metrics.api.WeeklyMetrics
import de.sambalmueslie.openevent.infrastructure.metrics.db.DailyMetricsRepository
import de.sambalmueslie.openevent.infrastructure.metrics.db.WeeklyMetricsRepository
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.time.LocalDate
import kotlin.reflect.KClass

@Singleton
class MetricsService(
    private val writer: BufferedMetricsWriter,
    private val dailyRepository: DailyMetricsRepository,
    private val weeklyRepository: WeeklyMetricsRepository,
    private val auditService: AuditService
) {

    companion object {
        private val logger = LoggerFactory.getLogger(MetricsService::class.java)
    }

    fun <T : Any> getProbe(source: MetricsSource, logger: String, type: KClass<T>): MetricsProbe {
        val l = auditService.getLogger(logger)
        return MetricsProbeImpl(writer, source, type.simpleName ?: "unknown", l)
    }


    fun getDaily(type: String, action: String, from: LocalDate, to: LocalDate): List<DailyMetrics> {
        return dailyRepository.findByTypeAndActionAndTimestampBetween(type, action, from, to).map { it.convert() }
    }

    fun getWeekly(type: String, action: String, from: LocalDate, to: LocalDate): List<WeeklyMetrics> {
        return weeklyRepository.findByTypeAndActionAndTimestampBetween(type, action, from, to).map { it.convert() }
    }

}