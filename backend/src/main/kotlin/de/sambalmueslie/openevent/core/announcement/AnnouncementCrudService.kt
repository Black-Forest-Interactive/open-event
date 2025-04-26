package de.sambalmueslie.openevent.core.announcement


import de.sambalmueslie.openevent.common.BaseCrudService
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.announcement.api.Announcement
import de.sambalmueslie.openevent.core.announcement.api.AnnouncementChangeRequest
import de.sambalmueslie.openevent.core.announcement.db.AnnouncementStorage
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class AnnouncementCrudService(
    private val storage: AnnouncementStorage
) : BaseCrudService<Long, Announcement, AnnouncementChangeRequest, AnnouncementChangeListener>(storage) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AnnouncementCrudService::class.java)
    }

    fun create(author: Account, request: AnnouncementChangeRequest): Announcement {
        val result = storage.create(request, author)
        notifyCreated(author, result)
        return result
    }

}
