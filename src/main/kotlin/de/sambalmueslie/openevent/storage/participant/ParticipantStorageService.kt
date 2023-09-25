package de.sambalmueslie.openevent.storage.participant


import de.sambalmueslie.openevent.core.model.Account
import de.sambalmueslie.openevent.core.model.Participant
import de.sambalmueslie.openevent.core.model.ParticipantChangeRequest
import de.sambalmueslie.openevent.core.model.Registration
import de.sambalmueslie.openevent.core.storage.ParticipantStorage
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.infrastructure.cache.CacheService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import de.sambalmueslie.openevent.storage.BaseStorageService
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class ParticipantStorageService(
    private val repository: ParticipantRepository,
    private val converter: ParticipantConverter,
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

    override fun isValid(request: ParticipantChangeRequest) {
       if(request.size <= 0) throw InvalidRequestException("Size cannot be below zero")
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


}
