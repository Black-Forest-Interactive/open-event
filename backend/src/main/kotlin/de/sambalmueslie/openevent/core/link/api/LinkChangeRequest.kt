package de.sambalmueslie.openevent.core.link.api

import de.sambalmueslie.openevent.common.BusinessObjectChangeRequest

data class LinkChangeRequest(
    val enabled: Boolean,
    val params: Map<String, String>
) : BusinessObjectChangeRequest
