package de.sambalmueslie.openevent.testdata

import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.category.CategoryCrudService
import de.sambalmueslie.openevent.core.category.api.Category
import de.sambalmueslie.openevent.core.category.api.CategoryChangeRequest

/**
 * Reusable test data for categories. Provides request builders plus a persist helper so more complex
 * tests (e.g. events referencing [CategoryChangeRequest] via `categoryIds`) can obtain a persisted
 * [Category] and reuse its id.
 */
object CategoryTestData {

    fun createRequest(
        name: String = "name",
        iconUrl: String = "icon-url",
    ) = CategoryChangeRequest(name, iconUrl)

    fun updateRequest(
        name: String = "name-update",
        iconUrl: String = "icon-url-update",
    ) = CategoryChangeRequest(name, iconUrl)

    fun create(
        service: CategoryCrudService,
        actor: Account,
        request: CategoryChangeRequest = createRequest(),
    ): Category = service.create(actor, request)

}
