package de.sambalmueslie.openevent.gateway.backoffice.history

import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/backoffice/history")
@Tag(name = "BACKOFFICE History API")
class HistoryController(private val service: HistoryGuardService) {

    @Get("/{id}")
    fun get(auth: Authentication, id: Long) = service.get(auth, id)

    @Get()
    fun getAll(auth: Authentication, pageable: Pageable) = service.getAll(auth, pageable)

    @Get("/for/event/{eventId}")
    fun getForEvent(auth: Authentication, eventId: Long, pageable: Pageable) = service.getForEvent(auth, eventId, pageable)

    @Get("/info")
    fun getInfos(auth: Authentication, pageable: Pageable) = service.getInfos(auth, pageable)
}
