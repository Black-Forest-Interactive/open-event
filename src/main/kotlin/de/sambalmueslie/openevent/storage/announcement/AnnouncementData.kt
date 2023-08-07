package de.sambalmueslie.openevent.storage.announcement

import de.sambalmueslie.openevent.core.model.Account
import de.sambalmueslie.openevent.core.model.Announcement
import de.sambalmueslie.openevent.core.model.AnnouncementChangeRequest
import de.sambalmueslie.openevent.storage.DataObject
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "Announcement")
@Table(name = "announcement")
data class AnnouncementData(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long = 0,
    @Column var subject: String = "",
    @Column var content: String = "",
    @Column var authorId: Long = 0,

    @Column var created: LocalDateTime = LocalDateTime.now(),
    @Column var updated: LocalDateTime? = null
) : DataObject {
    companion object {
        fun create(
            account: Account,
            request: AnnouncementChangeRequest,
            timestamp: LocalDateTime
        ): AnnouncementData {
            return AnnouncementData(0, request.subject, request.content, account.id, timestamp)
        }
    }

    fun convert(account: Account): Announcement {
        return Announcement(id, subject, content, account, created)
    }

    fun update(request: AnnouncementChangeRequest, timestamp: LocalDateTime): AnnouncementData {
        subject = request.subject
        content = request.content
        updated = timestamp
        return this
    }
}

