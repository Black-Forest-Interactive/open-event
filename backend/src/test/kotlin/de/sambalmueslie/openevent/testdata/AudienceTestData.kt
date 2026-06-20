package de.sambalmueslie.openevent.testdata

import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.audience.AudienceCrudService
import de.sambalmueslie.openevent.core.audience.api.Audience
import de.sambalmueslie.openevent.core.audience.api.AudienceChangeRequest

object AudienceTestData {

    fun createRequest(name: String = "name", iconUrl: String = "icon-url") =
        AudienceChangeRequest(name, iconUrl)

    fun updateRequest(name: String = "name-update", iconUrl: String = "icon-url-update") =
        AudienceChangeRequest(name, iconUrl)

    fun create(
        service: AudienceCrudService,
        actor: Account,
        request: AudienceChangeRequest = createRequest(),
    ): Audience = service.create(actor, request)

}
