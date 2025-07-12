package de.sambalmueslie.openevent.core.share.api

import de.sambalmueslie.openevent.common.BusinessObjectChangeRequest

data class ShareChangeRequest(
    val enabled: Boolean
) : BusinessObjectChangeRequest