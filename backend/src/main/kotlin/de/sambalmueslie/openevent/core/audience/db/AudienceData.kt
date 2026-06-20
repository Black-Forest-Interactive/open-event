package de.sambalmueslie.openevent.core.audience.db

import de.sambalmueslie.openevent.common.SimpleDataObject
import de.sambalmueslie.openevent.core.audience.api.Audience
import de.sambalmueslie.openevent.core.audience.api.AudienceChangeRequest
import jakarta.persistence.*
import java.time.LocalDateTime


@Entity(name = "Audience")
@Table(name = "audience")
data class AudienceData(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long = 0,
    @Column var name: String = "",
    @Column var iconUrl: String = "",

    @Column var created: LocalDateTime = LocalDateTime.now(),
    @Column var updated: LocalDateTime? = null
) : SimpleDataObject<Audience> {
    companion object {
        fun create(
            request: AudienceChangeRequest,
            timestamp: LocalDateTime
        ): AudienceData {
            return AudienceData(0, request.name, request.iconUrl, timestamp)
        }
    }

    override fun convert(): Audience {
        return Audience(id, name, iconUrl)
    }

    fun update(request: AudienceChangeRequest, timestamp: LocalDateTime): AudienceData {
        name = request.name
        iconUrl = request.iconUrl
        updated = timestamp
        return this
    }
}

