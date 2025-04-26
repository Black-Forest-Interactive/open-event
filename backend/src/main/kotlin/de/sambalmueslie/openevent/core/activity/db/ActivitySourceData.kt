package de.sambalmueslie.openevent.core.activity.db

import de.sambalmueslie.openevent.common.SimpleDataObject
import de.sambalmueslie.openevent.core.activity.api.ActivitySource
import de.sambalmueslie.openevent.core.activity.api.ActivitySourceChangeRequest
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "ActivitySource")
@Table(name = "activity_source")
data class ActivitySourceData(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long = 0,
    @Column var key: String,

    @Column var created: LocalDateTime,
    @Column var updated: LocalDateTime? = null

) : SimpleDataObject<ActivitySource> {

    companion object {
        fun create(request: ActivitySourceChangeRequest, timestamp: LocalDateTime): ActivitySourceData {
            return ActivitySourceData(0, request.key, timestamp)
        }
    }

    override fun convert(): ActivitySource {
        return ActivitySource(id, key)
    }

    fun update(request: ActivitySourceChangeRequest, timestamp: LocalDateTime): ActivitySourceData {
        key = request.key
        updated = timestamp
        return this
    }
}
