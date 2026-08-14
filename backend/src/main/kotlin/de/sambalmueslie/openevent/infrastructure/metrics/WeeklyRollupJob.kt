package de.sambalmueslie.openevent.infrastructure.metrics

import de.sambalmueslie.openevent.common.findByIdOrNull
import de.sambalmueslie.openevent.infrastructure.metrics.db.*
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import io.micronaut.scheduling.annotation.Scheduled
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.system.measureTimeMillis

@Singleton
class WeeklyRollupJob(
    private val dailyRepository: DailyMetricsRepository,
    private val weeklyRepository: WeeklyMetricsRepository,
    private val timeProvider: TimeProvider,
) {

    companion object {
        private val logger = LoggerFactory.getLogger(WeeklyRollupJob::class.java)
    }

    @Scheduled(cron = "0 30 3 * * *")
    fun rollupCurrentWeek() {
        logger.info("Starting weekly metrics rollup")
        var counter = 0
        val duration = measureTimeMillis {
            val today = timeProvider.now().toLocalDate()
            val monday = today.with(DayOfWeek.MONDAY)
            val sunday = monday.plusDays(6)
            val groups = dailyRepository.findByTimestampBetween(monday, sunday).groupBy { Triple(it.type, it.action, it.resource) }
            groups.forEach { (key, rows) ->
                upsertWeekly(key, monday, rows)
                counter++
            }
        }
        logger.info("Finished weekly metrics rollup for $counter resource groups in $duration ms")
    }

    private fun upsertWeekly(key: Triple<String, String, Long>, monday: LocalDate, rows: List<DailyMetricsData>) {
        val (type, action, resource) = key
        val id = "$type-$action-$resource-${monday.format(DateTimeFormatter.ISO_LOCAL_DATE)}"
        val totalCount = rows.sumOf { it.totalCount }
        val uniqueCount = rows.sumOf { it.uniqueCount }
        val entries = rows.flatMap { it.entries }
            .groupBy { it.source }
            .map { (source, values) -> MetricsEntryData(source, values.sumOf { it.totalCount }, values.sumOf { it.uniqueCount }) }
        val breakdown = rows.sortedBy { it.timestamp }.map { DailyBreakdownEntryData(it.timestamp, it.entries) }
        val data = WeeklyMetricsData(id, type, resource, action, monday, totalCount, uniqueCount, entries, breakdown)
        if (weeklyRepository.findByIdOrNull(id) != null) weeklyRepository.update(data) else weeklyRepository.save(data)
    }

}
