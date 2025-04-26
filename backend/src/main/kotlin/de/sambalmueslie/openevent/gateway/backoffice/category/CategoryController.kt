package de.sambalmueslie.openevent.gateway.backoffice.category

import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.category.CategoryCrudService
import de.sambalmueslie.openevent.core.category.api.Category
import de.sambalmueslie.openevent.core.category.api.CategoryChangeRequest
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.search.SearchService
import de.sambalmueslie.openevent.core.search.api.CategorySearchRequest
import de.sambalmueslie.openevent.core.search.api.CategorySearchResponse
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/backoffice/category")
@Tag(name = "BACKOFFICE Category API")
class CategoryController(
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

    @Get("/{id}")
    fun get(auth: Authentication, id: Long): Category? {
        return auth.checkPermission(PERMISSION_ADMIN) { service.get(id) }
    }

    @Get("/find/by/name")
    fun findByName(auth: Authentication, @QueryValue name: String): Category? {
        return auth.checkPermission(PERMISSION_ADMIN) { service.findByName(name) }
    }

    @Get()
    fun getAll(auth: Authentication, pageable: Pageable): Page<Category> {
        return auth.checkPermission(PERMISSION_ADMIN) { service.getAll(pageable) }
    }

    @Post("search")
    fun searchCategories(auth: Authentication, @Body request: CategorySearchRequest, pageable: Pageable): CategorySearchResponse {
        return auth.checkPermission(PERMISSION_ADMIN) {
            searchService.searchCategories(
                accountService.find(auth),
                request,
                pageable
            )
        }
    }

    @Post()
    fun create(auth: Authentication, @Body request: CategoryChangeRequest): Category {
        return auth.checkPermission(PERMISSION_ADMIN) {
            logger.traceCreate(auth, request) { service.create(accountService.find(auth), request) }
        }
    }

    @Put("/{id}")
    fun update(auth: Authentication, id: Long, @Body request: CategoryChangeRequest): Category {
        return auth.checkPermission(PERMISSION_ADMIN) {
            logger.traceUpdate(auth, request) { service.update(accountService.find(auth), id, request) }
        }
    }

    @Delete("/{id}")
    fun delete(auth: Authentication, id: Long): Category? {
        return auth.checkPermission(PERMISSION_ADMIN) {
            logger.traceDelete(auth) { service.delete(accountService.find(auth), id) }
        }
    }

}