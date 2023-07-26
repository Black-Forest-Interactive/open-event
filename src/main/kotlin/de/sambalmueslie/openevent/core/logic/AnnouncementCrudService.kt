package de.sambalmueslie.openevent.core.logic


import de.sambalmueslie.openevent.core.BaseCrudService
import de.sambalmueslie.openevent.core.model.Account
import de.sambalmueslie.openevent.core.model.Announcement
import de.sambalmueslie.openevent.core.model.AnnouncementChangeRequest
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class AnnouncementCrudService(
    private val storage: AnnouncementStorage
) : BaseCrudService<Long, Announcement, AnnouncementChangeRequest>(storage, logger) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AnnouncementCrudService::class.java)
    }

    fun create(author: Account, request: AnnouncementChangeRequest): Announcement {
        val result = storage.create(request, author)
        notifyCreated(result)
        return result
    }

}
