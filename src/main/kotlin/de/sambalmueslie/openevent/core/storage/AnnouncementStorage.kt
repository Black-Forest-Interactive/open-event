package de.sambalmueslie.openevent.core.storage

import de.sambalmueslie.openevent.core.Storage
import de.sambalmueslie.openevent.core.model.Account
import de.sambalmueslie.openevent.core.model.Announcement
import de.sambalmueslie.openevent.core.model.AnnouncementChangeRequest

interface AnnouncementStorage : Storage<Long, Announcement, AnnouncementChangeRequest> {
    fun create(request: AnnouncementChangeRequest, author: Account): Announcement
}
