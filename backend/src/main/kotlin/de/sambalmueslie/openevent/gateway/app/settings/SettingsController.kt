package de.sambalmueslie.openevent.gateway.app.settings

import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import de.sambalmueslie.openevent.infrastructure.settings.SettingsService
import de.sambalmueslie.openevent.infrastructure.settings.api.TextResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/app/settings")
@Tag(name = "APP Settings API")
class SettingsController(
    private val service: SettingsService,
    private val accountService: AccountCrudService,
    audit: AuditService,
) {
    companion object {
        private const val PERMISSION_READ = "settings.read"
        private const val PERMISSION_WRITE = "settings.write"
    }


    private val logger = audit.getLogger("APP Settings API")

    @Get("title")
    fun getTitle(auth: Authentication) = TextResponse(service.getTitle())

}