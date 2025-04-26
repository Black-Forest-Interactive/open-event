package de.sambalmueslie.openevent.gateway.backoffice.cache

import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.infrastructure.cache.CacheInfo
import de.sambalmueslie.openevent.infrastructure.cache.CacheService
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/backoffice/cache")
@Tag(name = "BACKOFFICE Cache API")
class CacheController(private val service: CacheService) {
    companion object {
        private const val PERMISSION_READ = "cache.read"
        private const val PERMISSION_WRITE = "cache.write"
        private const val PERMISSION_ADMIN = "cache.admin"
    }

    @Get("/{key}")
     fun get(auth: Authentication, key: String): CacheInfo? {
        return auth.checkPermission(PERMISSION_ADMIN) { service.get(key) }
    }

    @Get()
     fun getAll(auth: Authentication): List<CacheInfo> {
        return auth.checkPermission(PERMISSION_ADMIN) { service.getAll() }
    }

    @Delete("/{key}")
     fun reset(auth: Authentication, key: String): CacheInfo? {
        return auth.checkPermission(PERMISSION_ADMIN) { service.reset(key) }
    }

    @Delete()
     fun resetAll(auth: Authentication): List<CacheInfo> {
        return auth.checkPermission(PERMISSION_ADMIN) { service.resetAll() }
    }

}