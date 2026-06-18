package de.sambalmueslie.openevent.core.search.audience

import de.sambalmueslie.openevent.core.audience.api.Audience
import kotlinx.serialization.Serializable

@Serializable
data class AudienceSearchEntryData(
    var id: String,
    var name: String,
) {
    companion object {


        fun create(obj: Audience): AudienceSearchEntryData {
            return AudienceSearchEntryData(
                obj.id.toString(),
                obj.name
            )
        }
    }
}
