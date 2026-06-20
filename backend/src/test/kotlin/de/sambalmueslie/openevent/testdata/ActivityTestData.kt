package de.sambalmueslie.openevent.testdata

import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.activity.ActivityCrudService
import de.sambalmueslie.openevent.core.activity.api.Activity
import de.sambalmueslie.openevent.core.activity.api.ActivityChangeRequest
import de.sambalmueslie.openevent.core.activity.api.ActivitySource
import de.sambalmueslie.openevent.core.activity.api.ActivityType

/**
 * Reusable test data for activities. An activity references an [ActivitySource] and [ActivityType] that
 * must already exist (the converter resolves them on read), so the persist helper takes both.
 */
object ActivityTestData {

    fun createRequest(title: String = "title", referenceId: Long = 1) =
        ActivityChangeRequest(title, referenceId)

    fun updateRequest(title: String = "title-update", referenceId: Long = 2) =
        ActivityChangeRequest(title, referenceId)

    fun create(
        service: ActivityCrudService,
        actor: Account,
        source: ActivitySource,
        type: ActivityType,
        request: ActivityChangeRequest = createRequest(),
    ): Activity = service.create(actor, request, source, type)

}
