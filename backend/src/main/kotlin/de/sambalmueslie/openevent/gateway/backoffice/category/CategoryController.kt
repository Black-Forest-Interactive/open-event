package de.sambalmueslie.openevent.gateway.backoffice.category

import de.sambalmueslie.openevent.core.category.api.CategoryChangeRequest
import de.sambalmueslie.openevent.core.search.api.CategorySearchRequest
import de.sambalmueslie.openevent.core.search.api.CategorySearchResponse
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/backoffice/category")
@Tag(name = "BACKOFFICE Category API")
class CategoryController(private val service: CategoryGuardService) {

    @Get("/{id}")
    fun get(auth: Authentication, id: Long) = service.get(auth, id)

    @Get("/find/by/name")
    fun findByName(auth: Authentication, @QueryValue name: String) = service.findByName(auth, name)

    @Get()
    fun getAll(auth: Authentication, pageable: Pageable) = service.getAll(auth, pageable)

    @Post("search")
    fun searchCategories(auth: Authentication, @Body request: CategorySearchRequest, pageable: Pageable): CategorySearchResponse = service.searchCategories(auth, request, pageable)

    @Post()
    fun create(auth: Authentication, @Body request: CategoryChangeRequest) = service.create(auth, request)

    @Put("/{id}")
    fun update(auth: Authentication, id: Long, @Body request: CategoryChangeRequest) = service.update(auth, id, request)

    @Delete("/{id}")
    fun delete(auth: Authentication, id: Long) = service.delete(auth, id)
}
