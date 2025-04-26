package de.sambalmueslie.openevent.core.category.api

import de.sambalmueslie.openevent.common.BusinessObject
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class Category(
    override val id: Long,
    val name: String,
    val iconUrl: String
) : BusinessObject<Long>
