package de.sambalmueslie.openevent.core.api


import de.sambalmueslie.openevent.api.CategoryAPI
import de.sambalmueslie.openevent.api.CategoryAPI.Companion.PERMISSION_ADMIN
import de.sambalmueslie.openevent.api.CategoryAPI.Companion.PERMISSION_READ
import de.sambalmueslie.openevent.api.CategoryAPI.Companion.PERMISSION_WRITE
import de.sambalmueslie.openevent.core.auth.checkPermission
import de.sambalmueslie.openevent.core.logic.account.AccountCrudService
import de.sambalmueslie.openevent.core.logic.category.CategoryCrudService
import de.sambalmueslie.openevent.core.logic.category.CategorySearchService
import de.sambalmueslie.openevent.core.model.Category
import de.sambalmueslie.openevent.core.model.CategoryChangeRequest
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/category")
@Tag(name = "Category API")
class CategoryController(
    private val service: CategoryCrudService,
    private val search: CategorySearchService,
    private val accountService: AccountCrudService,
    audit: AuditService,
) : CategoryAPI {
    private val logger = audit.getLogger("Category API")

    @Get("/{id}")
    override fun get(auth: Authentication, id: Long): Category? {
        return auth.checkPermission(PERMISSION_READ) { service.get(id) }
    }

    @Get("/find/by/name")
    override fun findByName(auth: Authentication, @QueryValue name: String): Category? {
        return auth.checkPermission(PERMISSION_READ) { service.findByName(name) }
    }

    @Get()
    override fun getAll(auth: Authentication, pageable: Pageable): Page<Category> {
        return auth.checkPermission(PERMISSION_READ) { service.getAll(pageable) }
    }

    @Post()
    override fun create(auth: Authentication, @Body request: CategoryChangeRequest): Category {
        return auth.checkPermission(PERMISSION_WRITE) {
            logger.traceCreate(auth, request) { service.create(accountService.find(auth), request) }
        }
    }

    @Put("/{id}")
    override fun update(auth: Authentication, id: Long, @Body request: CategoryChangeRequest): Category {
        return auth.checkPermission(PERMISSION_WRITE) {
            logger.traceUpdate(auth, request) { service.update(accountService.find(auth), id, request) }
        }
    }

    @Delete("/{id}")
    override fun delete(auth: Authentication, id: Long): Category? {
        return auth.checkPermission(PERMISSION_WRITE) {
            logger.traceDelete(auth) { service.delete(accountService.find(auth), id) }
        }
    }

    @Get("/search")
    override fun search(auth: Authentication, @QueryValue query: String, pageable: Pageable): Page<Category> {
        return auth.checkPermission(PERMISSION_READ) { search.search(query, pageable) }
    }

    @Post("/search")
    fun buildIndex(auth: Authentication) {
        return auth.checkPermission(PERMISSION_ADMIN) {
            search.createIndex()
            HttpResponse.created("")
        }

    }
}
