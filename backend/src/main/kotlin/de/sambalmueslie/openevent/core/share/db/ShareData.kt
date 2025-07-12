package de.sambalmueslie.openevent.core.share.db

import de.sambalmueslie.openevent.common.SimpleDataObject
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.share.api.Share
import de.sambalmueslie.openevent.core.share.api.ShareChangeRequest
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.*

@Entity(name = "Share")
@Table(name = "share")
data class ShareData(
    @Id var id: String = UUID.randomUUID().toString(),

    @Column var eventId: Long,
    @Column var enabled: Boolean,

    @Column var created: LocalDateTime = LocalDateTime.now(),
    @Column var updated: LocalDateTime? = null
) : SimpleDataObject<Share> {

    companion object {
        fun create(
            event: Event,
            request: ShareChangeRequest,
            timestamp: LocalDateTime
        ): ShareData {
            return ShareData(
                UUID.randomUUID().toString(),
                event.id,
                request.enabled,
                timestamp
            )
        }
    }

    fun update(request: ShareChangeRequest, timestamp: LocalDateTime): ShareData {
        enabled = request.enabled
        updated = timestamp
        return this
    }

    fun setPublished(value: Boolean, timestamp: LocalDateTime): ShareData {
        this.enabled = value
        updated = timestamp
        return this
    }

    override fun convert(): Share {
        return Share(id, eventId, enabled, created, updated)
    }

}
