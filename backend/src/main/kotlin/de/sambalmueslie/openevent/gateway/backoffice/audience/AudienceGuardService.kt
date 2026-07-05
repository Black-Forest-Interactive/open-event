package de.sambalmueslie.openevent.gateway.backoffice.audience

import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.audience.AudienceCrudService
import de.sambalmueslie.openevent.core.audience.api.AudienceChangeRequest
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.search.SearchService
import de.sambalmueslie.openevent.core.search.api.AudienceSearchRequest
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton

@Singleton
class AudienceGuardService(
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

    fun get(auth: Authentication, id: Long) = auth.checkPermission(PERMISSION_ADMIN) { service.get(id) }

    fun findByName(auth: Authentication, name: String) = auth.checkPermission(PERMISSION_ADMIN) { service.findByName(name) }

    fun getAll(auth: Authentication, pageable: Pageable) = auth.checkPermission(PERMISSION_ADMIN) { service.getAll(pageable) }

    fun searchAudiences(auth: Authentication, request: AudienceSearchRequest, pageable: Pageable) =
        auth.checkPermission(PERMISSION_ADMIN) {
            searchService.searchAudiences(
                accountService.find(auth),
                request,
                pageable
            )
        }

    fun create(auth: Authentication, request: AudienceChangeRequest) =
        auth.checkPermission(PERMISSION_ADMIN) {
            logger.traceCreate(auth, request) { service.create(accountService.find(auth), request) }
        }

    fun update(auth: Authentication, id: Long, request: AudienceChangeRequest) =
        auth.checkPermission(PERMISSION_ADMIN) {
            logger.traceUpdate(auth, request) { service.update(accountService.find(auth), id, request) }
        }

    fun delete(auth: Authentication, id: Long) =
        auth.checkPermission(PERMISSION_ADMIN) {
            logger.traceDelete(auth) { service.delete(accountService.find(auth), id) }
        }
}
