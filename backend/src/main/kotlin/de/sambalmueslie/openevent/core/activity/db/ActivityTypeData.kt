package de.sambalmueslie.openevent.core.activity.db

import de.sambalmueslie.openevent.common.SimpleDataObject
import de.sambalmueslie.openevent.core.activity.api.ActivitySource
import de.sambalmueslie.openevent.core.activity.api.ActivityType
import de.sambalmueslie.openevent.core.activity.api.ActivityTypeChangeRequest
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "ActivityType")
@Table(name = "activity_type")
data class ActivityTypeData(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long = 0,
    @Column var key: String,
    @Column var sourceId: Long,

    @Column var created: LocalDateTime,
    @Column var updated: LocalDateTime? = null
) : SimpleDataObject<ActivityType> {

    companion object {
        fun create(request: ActivityTypeChangeRequest, source: ActivitySource, timestamp: LocalDateTime): ActivityTypeData {
            return ActivityTypeData(0, request.key, source.id, timestamp)
        }
    }

    override fun convert(): ActivityType {
        return ActivityType(id, key)
    }

    fun update(request: ActivityTypeChangeRequest, timestamp: LocalDateTime): ActivityTypeData {
        key = request.key
        updated = timestamp
        return this
    }
}
