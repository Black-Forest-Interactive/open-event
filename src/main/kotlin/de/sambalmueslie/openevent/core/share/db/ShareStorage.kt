package de.sambalmueslie.openevent.core.share.db

import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.common.Storage
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.share.api.Share
import de.sambalmueslie.openevent.core.share.api.ShareChangeRequest
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable

interface ShareStorage : Storage<String, Share, ShareChangeRequest> {
    fun create(request: ShareChangeRequest, event: Event, owner: Account): Share

    fun setPublished(id: String, value: PatchRequest<Boolean>): Share?
    fun findByEvent(event: Event): Share?
    fun getAllForAccount(account: Account, pageable: Pageable): Page<Share>
}