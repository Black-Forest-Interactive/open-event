package de.sambalmueslie.openevent.core.storage

import de.sambalmueslie.openevent.core.Storage
import de.sambalmueslie.openevent.core.model.NotificationScheme
import de.sambalmueslie.openevent.core.model.NotificationSchemeChangeRequest
import de.sambalmueslie.openevent.core.model.PatchRequest

interface NotificationSchemeStorage : Storage<Long, NotificationScheme, NotificationSchemeChangeRequest> {
    fun setEnabled(id: Long, value: PatchRequest<Boolean>): NotificationScheme?

}
