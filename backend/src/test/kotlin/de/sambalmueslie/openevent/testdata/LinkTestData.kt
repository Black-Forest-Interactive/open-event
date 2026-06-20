package de.sambalmueslie.openevent.testdata

import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.link.LinkCrudService
import de.sambalmueslie.openevent.core.link.api.Link
import de.sambalmueslie.openevent.core.link.api.LinkChangeRequest

object LinkTestData {

    fun createRequest(enabled: Boolean = true, params: Map<String, String> = mapOf("a" to "b")) =
        LinkChangeRequest(enabled, params)

    fun updateRequest(enabled: Boolean = false, params: Map<String, String> = mapOf("c" to "d")) =
        LinkChangeRequest(enabled, params)

    fun create(
        service: LinkCrudService,
        actor: Account,
        key: String = "link-key",
        request: LinkChangeRequest = createRequest(),
    ): Link = service.create(actor, key, request)

}
