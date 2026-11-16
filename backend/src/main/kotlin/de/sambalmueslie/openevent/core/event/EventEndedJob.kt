package de.sambalmueslie.openevent.core.event

import de.sambalmueslie.openevent.common.PageableSequence
import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.event.api.EventStatus
import de.sambalmueslie.openevent.core.event.db.EventRepository
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import io.micronaut.scheduling.annotation.Scheduled
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import kotlin.system.measureTimeMillis

@Singleton
class EventEndedJob(
    private val accountService: AccountCrudService,
    private val repository: EventRepository,
    private val service: EventCrudService,
    private val timeProvider: TimeProvider
) {

    companion object {
        private val logger = LoggerFactory.getLogger(EventEndedJob::class.java)
    }

    @Scheduled(cron = "0 3 * * *")
    fun updateEndedEvents() {
        logger.info("Starting update ended-job")
        var counter = 0
        val duration = measureTimeMillis {
            val actor = accountService.getSystemAccount()
            val timestamp = timeProvider.now().toLocalDate().atStartOfDay()
            val sequence = PageableSequence() { repository.findByStatusAndFinishBefore(EventStatus.ACTIVE, timestamp, it) }
            sequence.forEach { e ->
                service.setStatus(actor, e.id, EventStatus.ENDED)
                counter++
            }
        }
        logger.info("Ending update ended-job for $counter elements finished in $duration ms")
    }
}