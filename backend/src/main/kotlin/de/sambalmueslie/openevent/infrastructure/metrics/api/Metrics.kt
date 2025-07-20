package de.sambalmueslie.openevent.infrastructure.metrics.api

import de.sambalmueslie.openevent.common.BusinessObject
import java.time.LocalDate


data class Metrics(
    override val id: String,
    val resource: String,
    val action: String,
    val timestamp: LocalDate,
    val totalCount: Int,
    val uniqueCount: Int,
) : BusinessObject<String>
