package de.sambalmueslie.openevent.core.link

import de.sambalmueslie.openevent.common.BaseCrudService
import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.link.api.Link
import de.sambalmueslie.openevent.core.link.api.LinkChangeRequest
import de.sambalmueslie.openevent.core.link.db.LinkStorage
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class LinkCrudService(
    private val storage: LinkStorage,
) : BaseCrudService<String, Link, LinkChangeRequest, LinkChangeListener>(storage) {


    companion object {
        private val logger: Logger = LoggerFactory.getLogger(LinkCrudService::class.java)
    }

    fun create(actor: Account, key: String, request: LinkChangeRequest): Link {
        val result = storage.create(key, request)
        notifyCreated(actor, result)
        return result
    }

    fun setEnabled(actor: Account, key: String, value: PatchRequest<Boolean>): Link? {
        val link = storage.findByKey(key)
        if (link != null) {
            val result = storage.setEnabled(link.id, value) ?: return null
            notify { it.enabledChanged(actor, result) }
            return result
        } else {
            return create(actor, key, LinkChangeRequest(value.value, emptyMap()))
        }
    }

    override fun isValid(request: LinkChangeRequest) {
        // intentionally left empty
    }

    fun findByKeys(keys: Set<String>): List<Link> {
        return storage.findByKeys(keys)
    }

    fun findByKey(key: String): Link? {
        return storage.findByKey(key)
    }

}