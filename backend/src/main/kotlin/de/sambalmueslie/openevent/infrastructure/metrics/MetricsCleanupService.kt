package de.sambalmueslie.openevent.infrastructure.metrics

import de.sambalmueslie.openevent.config.MetricsConfig
import de.sambalmueslie.openevent.infrastructure.metrics.db.DailyMetricsRepository
import de.sambalmueslie.openevent.infrastructure.metrics.db.MetricsTraceRepository
import de.sambalmueslie.openevent.infrastructure.metrics.db.WeeklyMetricsRepository
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import de.sambalmueslie.openevent.measureTimeMillisWithValue
import io.micronaut.scheduling.annotation.Scheduled
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.system.measureTimeMillis

@Singleton
class MetricsCleanupService(
    private val traceRepository: MetricsTraceRepository,
    private val dailyRepository: DailyMetricsRepository,
    private val weeklyRepository: WeeklyMetricsRepository,
    private val timeProvider: TimeProvider,
    private val config: MetricsConfig
) {

    companion object {
        private val logger = LoggerFactory.getLogger(MetricsCleanupService::class.java)
    }

    @Scheduled(cron = "0 0 3 * * *") // Every day at 3am
    fun cleanupOldMetrics() {
        logger.info("Cleaning up old metrics")

        val timestamp = timeProvider.now().toLocalDate()
        val duration = measureTimeMillis {
            cleanupTraceMetrics(timestamp)
            cleanupDailyMetrics(timestamp)
            cleanupWeeklyMetrics(timestamp)
        }
        logger.info("Finished cleaning up old metrics within $duration ms")
    }

    private fun cleanupTraceMetrics(timestamp: LocalDate) {
        val threshold = timestamp.minusDays(config.traceExpirationDays).atStartOfDay()
        logger.info("Remove trace metrics before ${threshold.format(DateTimeFormatter.ISO_DATE_TIME)}")
        val (duration, removed) = measureTimeMillisWithValue { traceRepository.deleteByTimestampLessThan(threshold) }
        logger.info("Removed $removed old trace metrics within $duration ms")
    }


    private fun cleanupDailyMetrics(timestamp: LocalDate) {
        val threshold = timestamp.minusDays(config.dailyExpirationDays)
        logger.info("Remove daily metrics before ${threshold.format(DateTimeFormatter.ISO_DATE)}")
        val (duration, removed) = measureTimeMillisWithValue { dailyRepository.deleteByTimestampLessThan(threshold) }
        logger.info("Removed $removed old daily metrics within $duration ms")
    }


    private fun cleanupWeeklyMetrics(timestamp: LocalDate) {
        val threshold = timestamp.minusDays(config.weeklyExpirationDays)
        logger.info("Remove weekly metrics before ${threshold.format(DateTimeFormatter.ISO_DATE)}")
        val (duration, removed) = measureTimeMillisWithValue { weeklyRepository.deleteByTimestampLessThan(threshold) }
        logger.info("Removed $removed old weekly metrics within $duration ms")
    }
}