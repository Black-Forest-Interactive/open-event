package de.sambalmueslie.openevent.core.audience

import de.sambalmueslie.openevent.common.BaseCrudService
import de.sambalmueslie.openevent.core.audience.api.Audience
import de.sambalmueslie.openevent.core.audience.api.AudienceChangeRequest
import de.sambalmueslie.openevent.core.audience.db.AudienceStorage
import de.sambalmueslie.openevent.error.InvalidRequestException
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory


@Singleton
class AudienceCrudService(
    private val storage: AudienceStorage
) : BaseCrudService<Long, Audience, AudienceChangeRequest, AudienceChangeListener>(storage) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AudienceCrudService::class.java)
    }

    fun findByName(name: String): Audience? {
        return storage.findByName(name)
    }

    override fun isValid(request: AudienceChangeRequest) {
        if (request.name.isBlank()) throw InvalidRequestException("Name cannot be blank")
    }

}
