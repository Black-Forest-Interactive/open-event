package de.sambalmueslie.openevent.core.event.api

import de.sambalmueslie.openevent.core.audience.api.Audience
import de.sambalmueslie.openevent.core.category.api.Category
import de.sambalmueslie.openevent.core.location.api.Location
import de.sambalmueslie.openevent.core.registration.api.RegistrationInfo
import de.sambalmueslie.openevent.core.share.api.ShareInfo

data class EventInfo(
    val event: Event,
    val location: Location?,
    val registration: RegistrationInfo?,
    val audiences: List<Audience>,
    val categories: List<Category>,
    val share: ShareInfo?,
    val bookmarked: Boolean,
    val canEdit: Boolean
)