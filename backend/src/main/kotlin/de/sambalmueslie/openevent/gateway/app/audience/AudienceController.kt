package de.sambalmueslie.openevent.gateway.app.audience

import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.audience.AudienceCrudService
import de.sambalmueslie.openevent.core.audience.api.Audience
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/app/audience")
@Tag(name = "APP Audience API")
class AudienceController(
    private val service: AudienceCrudService,
    private val accountService: AccountCrudService,
    audit: AuditService,
) {
    companion object {
        private const val PERMISSION_READ = "audience.read"
    }

    private val logger = audit.getLogger("APP Audience API")

    @Get("/{id}")
    fun get(auth: Authentication, id: Long): Audience? {
        return auth.checkPermission(PERMISSION_READ) { service.get(id) }
    }

    @Get()
    fun getAll(auth: Authentication, pageable: Pageable): Page<Audience> {
        return auth.checkPermission(PERMISSION_READ) { service.getAll(pageable) }
    }
}
