package de.sambalmueslie.openevent.gateway.backoffice.audience

import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.audience.AudienceCrudService
import de.sambalmueslie.openevent.core.audience.api.Audience
import de.sambalmueslie.openevent.core.audience.api.AudienceChangeRequest
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.search.SearchService
import de.sambalmueslie.openevent.core.search.api.AudienceSearchRequest
import de.sambalmueslie.openevent.core.search.api.AudienceSearchResponse
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/backoffice/audience")
@Tag(name = "BACKOFFICE Audience API")
class AudienceController(
    private val service: AudienceCrudService,
    private val searchService: SearchService,
    private val accountService: AccountCrudService,
    audit: AuditService,
) {
    companion object {
        private const val PERMISSION_READ = "audience.read"
        private const val PERMISSION_WRITE = "audience.write"
        private const val PERMISSION_ADMIN = "audience.admin"
    }

    private val logger = audit.getLogger("Audience API")

    @Get("/{id}")
    fun get(auth: Authentication, id: Long): Audience? {
        return auth.checkPermission(PERMISSION_ADMIN) { service.get(id) }
    }

    @Get("/find/by/name")
    fun findByName(auth: Authentication, @QueryValue name: String): Audience? {
        return auth.checkPermission(PERMISSION_ADMIN) { service.findByName(name) }
    }

    @Get()
    fun getAll(auth: Authentication, pageable: Pageable): Page<Audience> {
        return auth.checkPermission(PERMISSION_ADMIN) { service.getAll(pageable) }
    }

    @Post("search")
    fun searchAudiences(auth: Authentication, @Body request: AudienceSearchRequest, pageable: Pageable): AudienceSearchResponse {
        return auth.checkPermission(PERMISSION_ADMIN) {
            searchService.searchAudiences(
                accountService.find(auth),
                request,
                pageable
            )
        }
    }

    @Post()
    fun create(auth: Authentication, @Body request: AudienceChangeRequest): Audience {
        return auth.checkPermission(PERMISSION_ADMIN) {
            logger.traceCreate(auth, request) { service.create(accountService.find(auth), request) }
        }
    }

    @Put("/{id}")
    fun update(auth: Authentication, id: Long, @Body request: AudienceChangeRequest): Audience {
        return auth.checkPermission(PERMISSION_ADMIN) {
            logger.traceUpdate(auth, request) { service.update(accountService.find(auth), id, request) }
        }
    }

    @Delete("/{id}")
    fun delete(auth: Authentication, id: Long): Audience? {
        return auth.checkPermission(PERMISSION_ADMIN) {
            logger.traceDelete(auth) { service.delete(accountService.find(auth), id) }
        }
    }
}
