package de.sambalmueslie.openevent.core.share.db

import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.common.Storage
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.share.api.Share
import de.sambalmueslie.openevent.core.share.api.ShareChangeRequest

interface ShareStorage : Storage<String, Share, ShareChangeRequest> {
    fun create(event: Event, request: ShareChangeRequest): Share

    fun setEnabled(id: String, value: PatchRequest<Boolean>): Share?
    fun findByEvent(event: Event): Share?
    fun findByEventIds(eventIds: Set<Long>): List<Share>
}