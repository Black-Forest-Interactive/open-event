package de.sambalmueslie.openevent.core.link.db

import de.sambalmueslie.openevent.common.SimpleDataObject
import de.sambalmueslie.openevent.core.link.api.Link
import de.sambalmueslie.openevent.core.link.api.LinkChangeRequest
import io.micronaut.data.annotation.TypeDef
import io.micronaut.data.model.DataType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.*

@Entity(name = "Link")
@Table(name = "link")
data class LinkData(
    @Id var id: String = UUID.randomUUID().toString(),
    @Column var key: String,
    @Column var enabled: Boolean,
    @Column @field:TypeDef(type = DataType.JSON) var params: Map<String, String> = mapOf(),

    @Column var created: LocalDateTime = LocalDateTime.now(),
    @Column var updated: LocalDateTime? = null
) : SimpleDataObject<Link> {


    companion object {
        fun create(key: String, request: LinkChangeRequest, timestamp: LocalDateTime): LinkData {
            return LinkData(
                UUID.randomUUID().toString(),
                key,
                request.enabled,
                request.params,
                timestamp
            )
        }
    }

    fun update(request: LinkChangeRequest, timestamp: LocalDateTime): LinkData {
        enabled = request.enabled
        params = request.params
        updated = timestamp
        return this
    }

    fun setPublished(value: Boolean, timestamp: LocalDateTime): LinkData {
        this.enabled = value
        updated = timestamp
        return this
    }

    override fun convert(): Link {
        return Link(id, key, enabled, params, created, updated)
    }

}
