package de.sambalmueslie.openevent.core.link.db

import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.common.Storage
import de.sambalmueslie.openevent.core.link.api.Link
import de.sambalmueslie.openevent.core.link.api.LinkChangeRequest

interface LinkStorage : Storage<String, Link, LinkChangeRequest> {
    fun create(key: String, request: LinkChangeRequest): Link

    fun setEnabled(id: String, value: PatchRequest<Boolean>): Link?
    fun findByKeys(keys: Set<String>): List<Link>
    fun findByKey(key: String): Link?
}