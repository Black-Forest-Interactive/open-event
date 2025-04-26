package de.sambalmueslie.openevent.core.issue.api

import de.sambalmueslie.openevent.common.BusinessObjectChangeRequest
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class IssueChangeRequest(
    val subject: String,
    val description: String,

    val error: String,
    val url: String
) : BusinessObjectChangeRequest
