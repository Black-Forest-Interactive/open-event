package de.sambalmueslie.openevent.api

import de.sambalmueslie.openevent.core.announcement.api.Announcement
import de.sambalmueslie.openevent.core.announcement.api.AnnouncementChangeRequest

interface AnnouncementAPI : CrudAPI<Long, Announcement, AnnouncementChangeRequest> {
    companion object {
        const val PERMISSION_READ = "openevent.announcement.read"
        const val PERMISSION_WRITE = "openevent.announcement.write"
    }
}
