package de.sambalmueslie.openevent.storage.event

import de.sambalmueslie.openevent.core.model.Account
import de.sambalmueslie.openevent.core.model.Event
import de.sambalmueslie.openevent.core.model.EventChangeRequest
import de.sambalmueslie.openevent.storage.DataObject
import jakarta.persistence.*
import java.time.LocalDateTime


@Entity(name = "Event")
@Table(name = "event")
data class EventData(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long = 0,
    @Column var ownerId: Long,

    @Column var start: LocalDateTime,
    @Column var finish: LocalDateTime,

    @Column var title: String,
    @Column var shortText: String,
    @Column var longText: String,
    @Column var imageUrl: String,
    @Column var iconUrl: String,

    @Column var hasLocation: Boolean,
    @Column var hasRegistration: Boolean,
    @Column var published: Boolean,

    @Column var created: LocalDateTime = LocalDateTime.now(),
    @Column var updated: LocalDateTime? = null
) : DataObject {
    companion object {
        fun create(
            account: Account,
            request: EventChangeRequest,
            timestamp: LocalDateTime
        ): EventData {
            return EventData(
                0,
                account.id,
                request.start,
                request.finish,
                request.title,
                request.shortText,
                request.longText,
                request.imageUrl,
                request.iconUrl,
                request.location != null,
                true,
                false,
                timestamp
            )
        }
    }

    fun convert(account: Account): Event {
        return Event(
            id,
            account,
            start,
            finish,
            title,
            shortText,
            longText,
            imageUrl,
            iconUrl,
            hasLocation,
            hasRegistration,
            published
        )
    }

    fun update(request: EventChangeRequest, timestamp: LocalDateTime): EventData {
        start = request.start
        finish = request.finish
        title = request.title
        shortText = request.shortText
        longText = request.longText
        imageUrl = request.imageUrl
        iconUrl = request.iconUrl
        hasLocation = request.location != null
        updated = timestamp
        return this
    }

    fun setPublished(value: Boolean, timestamp: LocalDateTime): EventData {
        this.published = value
        updated = timestamp
        return this
    }
}


