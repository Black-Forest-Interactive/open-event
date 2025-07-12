package de.sambalmueslie.openevent.core.participant

import de.sambalmueslie.openevent.common.PageableSequence
import de.sambalmueslie.openevent.core.participant.db.ExternalParticipantData
import de.sambalmueslie.openevent.core.participant.db.ExternalParticipantRepository
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import io.micronaut.scheduling.annotation.Scheduled
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

@Singleton
class ExternalParticipantExpireService(
    private val repository: ExternalParticipantRepository,
    private val timeProvider: TimeProvider
) {

    companion object {
        private val logger = LoggerFactory.getLogger(ExternalParticipantExpireService::class.java)
    }

    @Scheduled(cron = "0 0 4 * * ?")
    fun detectExpiredExternalParticipants() {
        val timestamp = timeProvider.now()
        val sequence = PageableSequence { repository.findByExpiresLessThanEquals(timestamp, it) }
        sequence.forEach { handleExpiredExternalParticipant(it) }
    }

    private fun handleExpiredExternalParticipant(participant: ExternalParticipantData) {
        repository.delete(participant)
    }
}