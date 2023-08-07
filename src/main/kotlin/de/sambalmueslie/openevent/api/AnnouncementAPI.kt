package de.sambalmueslie.openevent.api

import de.sambalmueslie.openevent.core.model.Announcement
import de.sambalmueslie.openevent.core.model.AnnouncementChangeRequest

interface AnnouncementAPI : CrudAPI<Long, Announcement, AnnouncementChangeRequest> {
    companion object {
        const val PERMISSION_READ = "openevent.announcement.read"
        const val PERMISSION_WRITE = "openevent.announcement.write"
    }
}
