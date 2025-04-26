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
    @Column var sourceId: Long,
    @Column var typeId: Long,
    @Column var referenceId: Long,
    @Column var created: LocalDateTime,
    @Column var updated: LocalDateTime? = null
) : DataObject {
    fun convert(actor: AccountInfo, source: ActivitySource, type: ActivityType): Activity {
        return Activity(id, title, actor, source.key, referenceId, type.key, updated ?: created)
    }


    companion object {
        fun create(actor: Account, request: ActivityChangeRequest, source: ActivitySource, type: ActivityType, timestamp: LocalDateTime): ActivityData {
            return ActivityData(
                0,
                request.title,
                actor.id,
                source.id,
                type.id,
                request.referenceId,
                timestamp
            )
        }
    }

    fun update(request: ActivityChangeRequest, timestamp: LocalDateTime): ActivityData {
        title = request.title
        referenceId = request.referenceId
        updated = timestamp
        return this
    }

}
