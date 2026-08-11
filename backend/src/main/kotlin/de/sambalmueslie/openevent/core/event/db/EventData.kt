package de.sambalmueslie.openevent.core.event.db

import de.sambalmueslie.openevent.common.DataObject
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.api.AccountInfo
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.event.api.EventChangeRequest
import de.sambalmueslie.openevent.core.event.api.EventStatus
import de.sambalmueslie.openevent.core.event.api.EventUpdateTextRequest
import io.micronaut.data.annotation.TypeDef
import io.micronaut.data.model.DataType
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
    @Column var featured: Boolean,

    @Column var hasLocation: Boolean,
    @Column var hasRegistration: Boolean,
    @Column @Enumerated(value = EnumType.STRING) var status: EventStatus,
    @Column var published: Boolean,
    @Column @field:TypeDef(type = DataType.JSON) var tags: Set<String>,

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
                false,
                request.location != null,
                true,
                request.status,
                request.published,
                request.tags,
                timestamp
            )
        }
    }

    fun convert(account: AccountInfo): Event {
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
            featured,
            hasLocation,
            hasRegistration,
            status,
            published,
            tags,
            created,
            updated
        )
    }

    fun update(request: EventChangeRequest, timestamp: LocalDateTime): EventData {
        tags = request.tags

        start = request.start
        finish = request.finish
        title = request.title
        shortText = request.shortText
        longText = request.longText
        imageUrl = request.imageUrl
        iconUrl = request.iconUrl
        status = request.status
        published = request.published
        hasLocation = request.location != null
        updated = timestamp
        return this
    }

    fun setFeatured(value: Boolean, timestamp: LocalDateTime): EventData {
        this.featured = value
        updated = timestamp
        return this
    }

    fun setStatus(value: EventStatus, timestamp: LocalDateTime): EventData {
        status = value
        updated = timestamp
        return this
    }

    fun setPublished(value: Boolean, timestamp: LocalDateTime): EventData {
        published = value
        updated = timestamp
        return this
    }

    fun setTitle(value: String, timestamp: LocalDateTime): EventData {
        this.title = value
        updated = timestamp
        return this
    }

    fun setShortText(value: String, timestamp: LocalDateTime): EventData {
        this.shortText = value
        updated = timestamp
        return this
    }

    fun setLongText(value: String, timestamp: LocalDateTime): EventData {
        this.longText = value
        updated = timestamp
        return this
    }

    fun setTags(value: Set<String>, timestamp: LocalDateTime): EventData {
        this.tags = value
        updated = timestamp
        return this
    }

    fun setText(request: EventUpdateTextRequest, timestamp: LocalDateTime): EventData {
        this.title = request.title
        this.shortText = request.shortText
        this.longText = request.longText
        updated = timestamp
        return this
    }
}


