package de.sambalmueslie.openevent.core.audience.api

import de.sambalmueslie.openevent.common.BusinessObjectChangeRequest

data class AudienceChangeRequest(
    val name: String,
    val iconUrl: String
) : BusinessObjectChangeRequest
