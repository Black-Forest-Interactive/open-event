package de.sambalmueslie.openevent.gateway.external.settings

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/external/settings")
@Tag(name = "Public Settings API")
@Secured(SecurityRule.IS_ANONYMOUS)
class SettingsController(
    private val service: ExternalSettingsService
) {

    @Get("title")
    fun getTitle() = service.getTitle()

    @Get("portal-url")
    fun getPortalUrl() = service.getPortalUrl()
}