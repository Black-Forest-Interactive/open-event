package de.sambalmueslie.openevent.gateway.backoffice.category

import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.category.CategoryCrudService
import de.sambalmueslie.openevent.core.category.api.CategoryChangeRequest
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.search.SearchService
import de.sambalmueslie.openevent.core.search.api.CategorySearchRequest
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton

@Singleton
class CategoryGuardService(
    private val service: CategoryCrudService,
    private val searchService: SearchService,
    private val accountService: AccountCrudService,
    audit: AuditService,
) {
    companion object {
        private const val PERMISSION_READ = "category.read"
        private const val PERMISSION_WRITE = "category.write"
        private const val PERMISSION_ADMIN = "category.admin"
    }

    private val logger = audit.getLogger("Category API")

    fun get(auth: Authentication, id: Long) = auth.checkPermission(PERMISSION_ADMIN) { service.get(id) }

    fun findByName(auth: Authentication, name: String) = auth.checkPermission(PERMISSION_ADMIN) { service.findByName(name) }

    fun getAll(auth: Authentication, pageable: Pageable) = auth.checkPermission(PERMISSION_ADMIN) { service.getAll(pageable) }

    fun searchCategories(auth: Authentication, request: CategorySearchRequest, pageable: Pageable) =
        auth.checkPermission(PERMISSION_ADMIN) {
            searchService.searchCategories(
                accountService.find(auth),
                request,
                pageable
            )
        }

    fun create(auth: Authentication, request: CategoryChangeRequest) =
        auth.checkPermission(PERMISSION_ADMIN) {
            logger.traceCreate(auth, request) { service.create(accountService.find(auth), request) }
        }

    fun update(auth: Authentication, id: Long, request: CategoryChangeRequest) =
        auth.checkPermission(PERMISSION_ADMIN) {
            logger.traceUpdate(auth, request) { service.update(accountService.find(auth), id, request) }
        }

    fun delete(auth: Authentication, id: Long) =
        auth.checkPermission(PERMISSION_ADMIN) {
            logger.traceDelete(auth) { service.delete(accountService.find(auth), id) }
        }
}
