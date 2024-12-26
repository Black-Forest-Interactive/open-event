package de.sambalmueslie.openevent.core.account


import de.sambalmueslie.openevent.api.LocationAPI.Companion.PERMISSION_READ
import de.sambalmueslie.openevent.api.LocationAPI.Companion.PERMISSION_WRITE
import de.sambalmueslie.openevent.api.ProfileAPI
import de.sambalmueslie.openevent.api.ProfileAPI.Companion.PERMISSION_ADMIN
import de.sambalmueslie.openevent.core.account.api.Profile
import de.sambalmueslie.openevent.core.account.api.ProfileChangeRequest
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.getRealmRoles
import de.sambalmueslie.openevent.error.InsufficientPermissionsException
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/profile")
@Tag(name = "Profile API")
class ProfileController(
    private val service: ProfileCrudService,
    private val accountService: AccountCrudService,
    audit: AuditService,
) : ProfileAPI {
    private val logger = audit.getLogger("Profile API")

    @Get("/{id}")
    override fun get(auth: Authentication, id: Long): Profile? {
        return auth.checkPermission(PERMISSION_READ, PERMISSION_ADMIN) {
            if (isAdmin(auth) || isOwn(auth, id)) {
                service.get(id)
            } else {
                null
            }
        }
    }

    @Get()
    override fun getAll(auth: Authentication, pageable: Pageable): Page<Profile> {
        return auth.checkPermission(PERMISSION_ADMIN) {
            service.getAll(pageable)
        }
    }

    @Post()
    override fun create(auth: Authentication, @Body request: ProfileChangeRequest): Profile {
        return auth.checkPermission(PERMISSION_ADMIN) {
            logger.traceCreate(auth, request) { service.create(accountService.find(auth), request) }
        }
    }

    @Put("/{id}")
    override fun update(auth: Authentication, id: Long, @Body request: ProfileChangeRequest): Profile {
        return auth.checkPermission(PERMISSION_WRITE, PERMISSION_ADMIN) {
            if (isAdmin(auth) || isOwn(auth, id)) {
                logger.traceUpdate(auth, request) { service.update(accountService.find(auth), id, request) }
            } else {
                throw InsufficientPermissionsException("No permission to access resource")
            }
        }
    }


    @Delete("/{id}")
    override fun delete(auth: Authentication, id: Long): Profile? {
        return auth.checkPermission(PERMISSION_WRITE, PERMISSION_ADMIN) {
            if (isAdmin(auth) || isOwn(auth, id)) {
                logger.traceDelete(auth) { service.delete(accountService.find(auth), id) }
            } else {
                throw InsufficientPermissionsException("No permission to access resource")
            }
        }
    }

    private fun isAdmin(auth: Authentication) = auth.getRealmRoles().contains(PERMISSION_ADMIN)

    private fun isOwn(auth: Authentication, id: Long): Boolean {
        val account = accountService.get(auth) ?: return false
        return service.getForAccount(account)?.id == id
    }
}
