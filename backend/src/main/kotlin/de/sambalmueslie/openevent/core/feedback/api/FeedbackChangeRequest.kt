package de.sambalmueslie.openevent.core.feedback.api

import de.sambalmueslie.openevent.common.BusinessObjectChangeRequest

data class FeedbackChangeRequest(
    val subject: String,
    val description: String,
    val topic: String,
    val tags: Set<String>,
    val rating: Int,
) : BusinessObjectChangeRequest
