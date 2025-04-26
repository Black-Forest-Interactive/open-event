package de.sambalmueslie.openevent.core.announcement.db

import de.sambalmueslie.openevent.common.Storage
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.announcement.api.Announcement
import de.sambalmueslie.openevent.core.announcement.api.AnnouncementChangeRequest

interface AnnouncementStorage : Storage<Long, Announcement, AnnouncementChangeRequest> {
    fun create(request: AnnouncementChangeRequest, author: Account): Announcement
}
