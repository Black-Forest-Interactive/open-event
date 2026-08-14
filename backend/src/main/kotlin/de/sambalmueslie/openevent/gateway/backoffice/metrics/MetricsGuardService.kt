package de.sambalmueslie.openevent.gateway.backoffice.metrics

import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.event.EventCrudService
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.infrastructure.metrics.MetricsProbeImpl.Companion.ACTION
import de.sambalmueslie.openevent.infrastructure.metrics.MetricsService
import de.sambalmueslie.openevent.infrastructure.metrics.WeeklyRollupJob
import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton
import java.time.LocalDate

@Singleton
class MetricsGuardService(
    private val service: MetricsService,
    private val eventService: EventCrudService,
    private val rollupJob: WeeklyRollupJob,
) {

    companion object {
        private const val PERMISSION_ADMIN = "metrics.admin"
        private val TYPE = Event::class.java.simpleName
    }

    fun getDaily(auth: Authentication, from: LocalDate, to: LocalDate): List<EventMetricsDaily> {
        return auth.checkPermission(PERMISSION_ADMIN) {
            val data = service.getDaily(TYPE, ACTION, from, to).groupBy { it.resource }
            val events = eventService.getInfoByIds(data.keys)
            events.map { EventMetricsDaily(it, (data[it.event.id] ?: emptyList())) }.filter { it.metrics.isNotEmpty() }
        }
    }

    fun getWeekly(auth: Authentication, from: LocalDate, to: LocalDate): List<EventMetricsWeekly> {
        return auth.checkPermission(PERMISSION_ADMIN) {
            val data =  service.getWeekly(TYPE, ACTION, from, to).groupBy { it.resource }
            val events = eventService.getInfoByIds(data.keys)
            events.map { EventMetricsWeekly(it, (data[it.event.id] ?: emptyList())) }.filter { it.metrics.isNotEmpty() }
        }
    }

    fun recalculateWeekly(auth: Authentication) {
        auth.checkPermission(PERMISSION_ADMIN) { rollupJob.rollupCurrentWeek() }
    }

}
