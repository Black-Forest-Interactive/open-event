package de.sambalmueslie.openevent.core.activity.db

import de.sambalmueslie.openevent.common.DataObject
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.api.AccountInfo
import de.sambalmueslie.openevent.core.activity.api.Activity
import de.sambalmueslie.openevent.core.activity.api.ActivityChangeRequest
import de.sambalmueslie.openevent.core.activity.api.ActivitySource
import de.sambalmueslie.openevent.core.activity.api.ActivityType
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "Activity")
@Table(name = "activity")
data class ActivityData(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long = 0,
    @Column var title: String,
    @Column var actorId: Long,
    @Column @Enumerated(EnumType.STRING) var source: ActivitySource,
    @Column var sourceId: Long,
    @Column @Enumerated(EnumType.STRING) var type: ActivityType,
    @Column var created: LocalDateTime,
    @Column var updated: LocalDateTime? = null
) : DataObject {
    fun convert(actor: AccountInfo): Activity {
        return Activity(id, title, actor, source, sourceId,  type, updated ?: created)
    }


    companion object {
        fun create(actor: Account, request: ActivityChangeRequest, timestamp: LocalDateTime): ActivityData {
            return ActivityData(
                0,
                request.title,
                actor.id,
                request.source,
                request.sourceId,
                request.type,
                timestamp
            )
        }
    }

    fun update(request: ActivityChangeRequest, timestamp: LocalDateTime): ActivityData {
        title = request.title
        source = request.source
        sourceId = request.sourceId
        type = request.type
        updated = timestamp
        return this
    }
}
