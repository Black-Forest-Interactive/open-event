package de.sambalmueslie.openevent.infrastructure.cache


import de.sambalmueslie.openevent.api.CacheAPI
import de.sambalmueslie.openevent.api.CacheAPI.Companion.PERMISSION_READ
import de.sambalmueslie.openevent.api.CacheAPI.Companion.PERMISSION_WRITE
import de.sambalmueslie.openevent.core.checkPermission
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/cache")
@Tag(name = "Cache API")
class CacheController(private val service: CacheService) : CacheAPI {
    @Get("/{key}")
    override fun get(auth: Authentication, key: String): CacheInfo? {
        return auth.checkPermission(PERMISSION_READ) { service.get(key) }
    }

    @Get()
    override fun getAll(auth: Authentication): List<CacheInfo> {
        return auth.checkPermission(PERMISSION_READ) { service.getAll() }
    }

    @Delete("/{key}")
    override fun reset(auth: Authentication, key: String): CacheInfo? {
        return auth.checkPermission(PERMISSION_WRITE) { service.reset(key) }
    }

    @Delete()
    override fun resetAll(auth: Authentication): List<CacheInfo> {
        return auth.checkPermission(PERMISSION_WRITE) { service.resetAll() }
    }


}
