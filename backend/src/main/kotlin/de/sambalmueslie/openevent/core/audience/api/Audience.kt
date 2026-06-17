package de.sambalmueslie.openevent.core.audience.api

import de.sambalmueslie.openevent.common.BusinessObject

data class Audience(
    override val id: Long,
    val name: String,
    val iconUrl: String
) : BusinessObject<Long>
