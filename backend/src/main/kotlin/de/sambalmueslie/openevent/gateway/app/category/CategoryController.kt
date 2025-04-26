package de.sambalmueslie.openevent.gateway.app.category

import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.category.CategoryCrudService
import de.sambalmueslie.openevent.core.category.api.Category
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/app/category")
@Tag(name = "APP Category API")
class CategoryController(
    private val service: CategoryCrudService,
    private val accountService: AccountCrudService,
    audit: AuditService,
) {
    companion object {
        private const val PERMISSION_READ = "category.read"
        private const val PERMISSION_WRITE = "category.write"
    }


    private val logger = audit.getLogger("APP Category API")

    @Get("/{id}")
    fun get(auth: Authentication, id: Long): Category? {
        return auth.checkPermission(PERMISSION_READ) { service.get(id) }
    }

    @Get()
    fun getAll(auth: Authentication, pageable: Pageable): Page<Category> {
        return auth.checkPermission(PERMISSION_READ) { service.getAll(pageable) }
    }

}