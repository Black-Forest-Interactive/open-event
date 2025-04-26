package de.sambalmueslie.openevent.core.activity.api

import de.sambalmueslie.openevent.common.BusinessObject
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class ActivitySource(
    override val id: Long,
    val key: String,
) : BusinessObject<Long> {
    companion object {
        fun default(): ActivitySource {
            return ActivitySource(0, "default")
        }
    }
}
