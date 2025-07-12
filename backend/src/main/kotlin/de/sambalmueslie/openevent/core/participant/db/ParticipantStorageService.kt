package de.sambalmueslie.openevent.core.participant.db


import de.sambalmueslie.openevent.common.BaseStorageService
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.participant.api.Participant
import de.sambalmueslie.openevent.core.participant.api.ParticipantChangeRequest
import de.sambalmueslie.openevent.core.participant.api.ParticipantDetails
import de.sambalmueslie.openevent.core.registration.api.Registration
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.infrastructure.cache.CacheService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class ParticipantStorageService(
    private val repository: ParticipantRepository,
    private val converter: ParticipantConverter,
    private val detailsConverter: ParticipantDetailsConverter,
    cacheService: CacheService,
    private val timeProvider: TimeProvider,
) : BaseStorageService<Long, Participant, ParticipantChangeRequest, ParticipantData>(
    repository,
    converter,
    cacheService,
    Participant::class,
    logger
), ParticipantStorage {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ParticipantStorageService::class.java)
        private const val AUTHOR_REFERENCE = "author"
        private const val REGISTRATION_REFERENCE = "registration"
    }

    override fun create(request: ParticipantChangeRequest, author: Account, registration: Registration): Participant {
        return create(
            request, mapOf(
                Pair(AUTHOR_REFERENCE, author),
                Pair(REGISTRATION_REFERENCE, registration),
            )
        )
    }

    override fun createData(request: ParticipantChangeRequest, properties: Map<String, Any>): ParticipantData {
        val account = properties[AUTHOR_REFERENCE] as? Account ?: throw InvalidRequestException("Cannot find account")
        val registration = properties[REGISTRATION_REFERENCE] as? Registration
            ?: throw InvalidRequestException("Cannot find registration")
        return ParticipantData.create(registration, account, request, timeProvider.now())
    }

    override fun updateData(data: ParticipantData, request: ParticipantChangeRequest): ParticipantData {
        return data.update(request, timeProvider.now())
    }

    override fun get(registration: Registration): List<Participant> {
        return repository.findByRegistrationId(registration.id).let { converter.convert(it) }
    }

    override fun findByAccount(registration: Registration, account: Account): Participant? {
        return repository.findByRegistrationIdAndAccountId(registration.id, account.id)?.let { converter.convert(it) }
    }

    override fun get(registration: List<Registration>): Map<Registration, List<Participant>> {
        val regIds = registration.map { it.id }.toSet()
        val participants = repository.findByRegistrationIdIn(regIds)
            .groupBy { it.registrationId }
            .mapValues { converter.convert(it.value).sortedBy { p -> p.rank } }
        return registration.associateWith { participants[it.id] ?: emptyList() }
    }

    override fun getDetails(registration: Registration): List<ParticipantDetails> {
        return repository.findByRegistrationId(registration.id).let { detailsConverter.convert(it) }
    }


}
