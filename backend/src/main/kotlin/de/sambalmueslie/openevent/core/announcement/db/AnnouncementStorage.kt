package de.sambalmueslie.openevent.core.announcement.db

import de.sambalmueslie.openevent.common.Storage
import de.sambalmueslie.openevent.core.announcement.api.Announcement
import de.sambalmueslie.openevent.core.announcement.api.AnnouncementChangeRequest

interface AnnouncementStorage : Storage<Long, Announcement, AnnouncementChangeRequest> {
}
