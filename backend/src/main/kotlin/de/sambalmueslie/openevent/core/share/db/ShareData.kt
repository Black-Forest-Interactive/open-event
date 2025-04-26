package de.sambalmueslie.openevent.core.share.db

import de.sambalmueslie.openevent.common.DataObject
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.api.AccountInfo
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.share.api.Share
import de.sambalmueslie.openevent.core.share.api.ShareChangeRequest
import io.micronaut.http.uri.UriBuilder
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
    @Column var ownerId: Long,

    @Column var published: Boolean,

    @Column var created: LocalDateTime = LocalDateTime.now(),
    @Column var updated: LocalDateTime? = null
) : DataObject {

    companion object {
        fun create(
            account: Account,
            event: Event,
            request: ShareChangeRequest,
            timestamp: LocalDateTime
        ): ShareData {
            return ShareData(
                UUID.randomUUID().toString(),
                event.id,
                account.id,
                request.published,
                timestamp
            )
        }
    }

    fun update(request: ShareChangeRequest, timestamp: LocalDateTime): ShareData {
        published = request.published
        updated = timestamp
        return this
    }

    fun setPublished(value: Boolean, timestamp: LocalDateTime): ShareData {
        this.published = value
        updated = timestamp
        return this
    }

    fun convert(owner: AccountInfo, baseUrl: String): Share {
        val url = UriBuilder.of(baseUrl).path("share").path("info").path(id)
            .queryParam("lang", "en")
            .toString()
        return Share(id, eventId, published, url, owner, created, updated)
    }
}
