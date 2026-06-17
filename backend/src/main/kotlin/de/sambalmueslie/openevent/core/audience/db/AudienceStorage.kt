package de.sambalmueslie.openevent.core.audience.db

import de.sambalmueslie.openevent.common.Storage
import de.sambalmueslie.openevent.core.audience.api.Audience
import de.sambalmueslie.openevent.core.audience.api.AudienceChangeRequest

interface AudienceStorage : Storage<Long, Audience, AudienceChangeRequest> {
    fun findByName(name: String): Audience?
}
