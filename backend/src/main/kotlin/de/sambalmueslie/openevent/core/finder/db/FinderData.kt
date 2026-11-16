package de.sambalmueslie.openevent.core.finder.db

import de.sambalmueslie.openevent.common.DataObject
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.finder.api.FinderChangeRequest
import de.sambalmueslie.openevent.core.search.api.EventSearchRequest
import io.micronaut.data.annotation.TypeDef
import io.micronaut.data.model.DataType
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "Finder")
@Table(name = "finder")
data class FinderData(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long = 0,
    @Column var ownerId: Long,
    @Column var name: String,

    @Column @field:TypeDef(type = DataType.JSON) var request: EventSearchRequest,

    @Column var created: LocalDateTime = LocalDateTime.now(),
    @Column var updated: LocalDateTime? = null
) : DataObject {
    companion object {
        fun create(account: Account, request: FinderChangeRequest, timestamp: LocalDateTime): FinderData {
            return FinderData(
                0,
                account.id,
                request.name,
                request.request,
                timestamp
            )
        }
    }

    fun update(request: FinderChangeRequest, timestamp: LocalDateTime): FinderData {
        this.name = request.name
        this.request = request.request
        updated = timestamp
        return this
    }
}
