package de.sambalmueslie.openevent.core.event.db

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import de.sambalmueslie.openevent.common.DataObject
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.api.AccountInfo
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.event.api.EventChangeRequest
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
    @Column(name = "tags") var tagsJson: String,

    @Column var created: LocalDateTime = LocalDateTime.now(),
    @Column var updated: LocalDateTime? = null
) : DataObject {
    companion object {
        private val mapper = ObjectMapper().registerKotlinModule()
        fun create(
            account: Account,
            request: EventChangeRequest,
            timestamp: LocalDateTime
        ): EventData {
            val t = mapper.writeValueAsString(request.tags)
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
                request.published,
                t,
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
            hasLocation,
            hasRegistration,
            published,
            getTags(),
            created,
            updated
        )
    }

    @Transient
    private var tags: Set<String>? = null

    @Transient
    fun getTags(): Set<String> {
        if (tags == null) {
            tags = mapper.readValue<Set<String>>(tagsJson)
        }
        return tags!!
    }

    fun update(request: EventChangeRequest, timestamp: LocalDateTime): EventData {
        val t = mapper.writeValueAsString(request.tags)
        tags = request.tags
        tagsJson = t

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


