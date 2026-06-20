package de.sambalmueslie.openevent.testdata

import de.sambalmueslie.openevent.core.share.api.ShareChangeRequest

/**
 * Reusable test data for shares. A share is auto-created for every event (EventCrudService.create), so
 * tests usually obtain it via ShareCrudService.findByEvent rather than creating one directly.
 */
object ShareTestData {

    fun createRequest(enabled: Boolean = true) = ShareChangeRequest(enabled)

    fun updateRequest(enabled: Boolean = false) = ShareChangeRequest(enabled)

}
