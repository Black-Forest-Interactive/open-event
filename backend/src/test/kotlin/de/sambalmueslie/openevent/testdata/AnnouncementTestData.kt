package de.sambalmueslie.openevent.testdata

import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.announcement.AnnouncementCrudService
import de.sambalmueslie.openevent.core.announcement.api.Announcement
import de.sambalmueslie.openevent.core.announcement.api.AnnouncementChangeRequest

object AnnouncementTestData {

    fun createRequest(subject: String = "subject", content: String = "content") =
        AnnouncementChangeRequest(subject, content)

    fun updateRequest(subject: String = "subject-update", content: String = "content-update") =
        AnnouncementChangeRequest(subject, content)

    fun create(
        service: AnnouncementCrudService,
        author: Account,
        request: AnnouncementChangeRequest = createRequest(),
    ): Announcement = service.create(author, request)

}
