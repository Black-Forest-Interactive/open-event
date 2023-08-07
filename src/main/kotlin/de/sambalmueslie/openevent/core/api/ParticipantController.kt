package de.sambalmueslie.openevent.core.api


import de.sambalmueslie.openevent.api.ParticipantAPI
import de.sambalmueslie.openevent.api.ParticipantAPI.Companion.PERMISSION_READ
import de.sambalmueslie.openevent.api.ParticipantAPI.Companion.PERMISSION_WRITE
import de.sambalmueslie.openevent.core.auth.checkPermission
import de.sambalmueslie.openevent.core.logic.participant.ParticipantCrudService
import de.sambalmueslie.openevent.core.model.Participant
import de.sambalmueslie.openevent.core.model.ParticipantChangeRequest
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/participant")
@Tag(name = "Participant API")
class ParticipantController(private val service: ParticipantCrudService) : ParticipantAPI {

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
        return auth.checkPermission(PERMISSION_WRITE) { service.create(request) }
    }

    @Put("/{id}")
    override fun update(auth: Authentication, id: Long, @Body request: ParticipantChangeRequest): Participant {
        return auth.checkPermission(PERMISSION_WRITE) { service.update(id, request) }
    }

    @Delete("/{id}")
    override fun delete(auth: Authentication, id: Long): Participant? {
        return auth.checkPermission(PERMISSION_WRITE) { service.delete(id) }
    }
}
