package de.sambalmueslie.openevent.gateway.backoffice.newsletter

import de.sambalmueslie.openevent.common.PatchRequest
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Put
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/backoffice/newsletter")
@Tag(name = "BACKOFFICE Newsletter API")
class NewsletterController(private val service: NewsletterGuardService) {

    @Get("/subscriber")
    fun getSubscribers(auth: Authentication, pageable: Pageable) = service.getSubscribers(auth, pageable)

    @Get("/setting")
    fun getSetting(auth: Authentication) = service.getSetting(auth)

    @Put("/setting/{id}/enabled")
    fun setSettingEnabled(auth: Authentication, id: Long, @Body value: PatchRequest<Boolean>) = service.setSettingEnabled(auth, id, value)
}
