package de.sambalmueslie.openevent.core.account


import de.sambalmueslie.openevent.api.LocationAPI.Companion.PERMISSION_READ
import de.sambalmueslie.openevent.api.LocationAPI.Companion.PERMISSION_WRITE
import de.sambalmueslie.openevent.api.PreferencesAPI
import de.sambalmueslie.openevent.api.PreferencesAPI.Companion.PERMISSION_ADMIN
import de.sambalmueslie.openevent.core.CoreAPI
import de.sambalmueslie.openevent.core.account.api.Preferences
import de.sambalmueslie.openevent.core.account.api.PreferencesChangeRequest
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.getRealmRoles
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/preferences")
@Tag(name = "Preferences API")
@CoreAPI
class PreferencesController(
    private val service: PreferencesCrudService,
    private val accountService: AccountCrudService,
    audit: AuditService,
) : PreferencesAPI {
    private val logger = audit.getLogger("Preferences API")

    @Get("/{id}")
    override fun get(auth: Authentication, id: Long): Preferences? {
        return auth.checkPermission(PERMISSION_READ, PERMISSION_ADMIN) {
            if (isAdmin(auth)) {
                service.get(id)
            } else {
                val account = accountService.get(auth) ?: return@checkPermission null
                val result = service.getForAccount(account) ?: return@checkPermission null
                if (result.id != id) return@checkPermission null
                result
            }
        }
    }


    @Get()
    override fun getAll(auth: Authentication, pageable: Pageable): Page<Preferences> {
        return auth.checkPermission(PERMISSION_ADMIN) {
            service.getAll(pageable)
        }
    }

    @Post()
    override fun create(auth: Authentication, @Body request: PreferencesChangeRequest): Preferences {
        return auth.checkPermission(PERMISSION_WRITE, PERMISSION_ADMIN) {
            logger.traceCreate(auth, request) { service.create(accountService.find(auth), request) }
        }
    }

    @Put("/{id}")
    override fun update(auth: Authentication, id: Long, @Body request: PreferencesChangeRequest): Preferences {
        return auth.checkPermission(PERMISSION_WRITE, PERMISSION_ADMIN) {
            logger.traceUpdate(auth, request) { service.update(accountService.find(auth), id, request) }
        }
    }

    @Delete("/{id}")
    override fun delete(auth: Authentication, id: Long): Preferences? {
        return auth.checkPermission(PERMISSION_WRITE, PERMISSION_ADMIN) {
            logger.traceDelete(auth) { service.delete(accountService.find(auth), id) }
        }
    }

    private fun isAdmin(auth: Authentication) = auth.getRealmRoles().contains(PERMISSION_ADMIN)
}
