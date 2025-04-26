package de.sambalmueslie.openevent.gateway.backoffice.search

import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.search.SearchService
import de.sambalmueslie.openevent.core.search.common.SearchOperatorInfo
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/backoffice/search")
@Tag(name = "BACKOFFICE Search API")
class SearchController(private val service: SearchService) {
    companion object {
        private const val PERMISSION_READ = "search.read"
        private const val PERMISSION_WRITE = "search.write"
        private const val PERMISSION_ADMIN = "search.admin"
    }


    @Get()
    fun getInfo(auth: Authentication): List<SearchOperatorInfo> {
        return auth.checkPermission(PERMISSION_ADMIN) {
            service.getInfo()
        }
    }

    @Get("/{key}")
    fun getInfo(auth: Authentication, key: String): SearchOperatorInfo? {
        return auth.checkPermission(PERMISSION_ADMIN) {
            service.getInfo(key)
        }
    }

    @Post("/{key}")
    fun setup(auth: Authentication, key: String): SearchOperatorInfo? {
        return auth.checkPermission(PERMISSION_ADMIN) {
            service.setup(key)
        }
    }
}