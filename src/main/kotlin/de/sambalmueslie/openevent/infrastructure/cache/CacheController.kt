package de.sambalmueslie.openevent.infrastructure.cache


import de.sambalmueslie.openevent.api.CacheAPI
import de.sambalmueslie.openevent.api.CacheAPI.Companion.PERMISSION_READ
import de.sambalmueslie.openevent.api.CacheAPI.Companion.PERMISSION_WRITE
import de.sambalmueslie.openevent.core.auth.checkPermission
import io.micronaut.http.annotation.Controller
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/cache")
@Tag(name = "Cache API")
class CacheController(private val service: CacheService) : CacheAPI {
    override fun get(auth: Authentication, key: String): CacheInfo? {
        return auth.checkPermission(PERMISSION_READ) { service.get(key) }
    }

    override fun getAll(auth: Authentication): List<CacheInfo> {
        return auth.checkPermission(PERMISSION_READ) { service.getAll() }
    }

    override fun reset(auth: Authentication, key: String): CacheInfo? {
        return auth.checkPermission(PERMISSION_WRITE) { service.reset(key) }
    }

    override fun resetAll(auth: Authentication): List<CacheInfo> {
        return auth.checkPermission(PERMISSION_WRITE) { service.resetAll() }
    }


}
