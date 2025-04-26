package de.sambalmueslie.openevent.core.participant


import de.sambalmueslie.openevent.api.ParticipantAPI
import de.sambalmueslie.openevent.api.ParticipantAPI.Companion.PERMISSION_READ
import de.sambalmueslie.openevent.api.ParticipantAPI.Companion.PERMISSION_WRITE
import de.sambalmueslie.openevent.core.CoreAPI
import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.participant.api.Participant
import de.sambalmueslie.openevent.core.participant.api.ParticipantChangeRequest
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/participant")
@Tag(name = "Participant API")
@CoreAPI
class ParticipantController(
    private val service: ParticipantCrudService,
    private val accountService: AccountCrudService,
    audit: AuditService,
) : ParticipantAPI {
    private val logger = audit.getLogger("Participant API")

    @Get("/{id}")
    override fun get(auth: Authentication, id: Long): Participant? {
        return auth.checkPermission(PERMISSION_READ) { service.get(id) }
    }

    @Get()
    override fun getAll(auth: Authentication, pageable: Pageable): Page<Participant> {
        return auth.checkPermission(PERMISSION_READ) { service.getAll(pageable) }
    }

    @Post()
    override fun create(auth: Authentication, @Body request: ParticipantChangeRequest): Participant {
        return auth.checkPermission(PERMISSION_WRITE) {
            logger.traceCreate(auth, request) { service.create(accountService.find(auth), request) }
        }
    }

    @Put("/{id}")
    override fun update(auth: Authentication, id: Long, @Body request: ParticipantChangeRequest): Participant {
        return auth.checkPermission(PERMISSION_WRITE) {
            logger.traceUpdate(auth, request) { service.update(accountService.find(auth), id, request) }
        }
    }

    @Delete("/{id}")
    override fun delete(auth: Authentication, id: Long): Participant? {
        return auth.checkPermission(PERMISSION_WRITE) {
            logger.traceDelete(auth) { service.delete(accountService.find(auth), id) }
        }
    }
}
