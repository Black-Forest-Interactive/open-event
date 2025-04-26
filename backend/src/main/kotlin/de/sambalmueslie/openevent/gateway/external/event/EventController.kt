package de.sambalmueslie.openevent.gateway.external.event

import de.sambalmueslie.openevent.core.event.EventCrudService
import de.sambalmueslie.openevent.core.share.ShareCrudService
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/public/event")
@Tag(name = "Public Event API")
class EventController(
    private val service: EventCrudService,
    private val shareService: ShareCrudService
) {

    @Get("{id}")
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun get(auth: Authentication?, id: String): PublicEvent? {
        val share = shareService.get(id) ?: return null
        val event = service.getInfo(share.eventId, null) ?: return null
        return event.toPublicEvent(share)
    }


}