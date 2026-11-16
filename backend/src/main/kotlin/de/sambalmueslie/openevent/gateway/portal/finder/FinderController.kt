package de.sambalmueslie.openevent.gateway.portal.finder

import de.sambalmueslie.openevent.core.finder.api.FinderAnswer
import de.sambalmueslie.openevent.core.finder.api.FinderAnswerRequest
import de.sambalmueslie.openevent.core.finder.api.FinderChangeRequest
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/portal/finder")
@Tag(name = "APP Finder API")
class FinderController(private val service: FinderGuardService) {

    @Get()
    fun get(auth: Authentication, pageable: Pageable) = service.get(auth, pageable)

    @Get("/{id}")
    fun get(auth: Authentication, id: Long) = service.get(auth, id)

    @Post()
    fun create(auth: Authentication, @Body request: FinderChangeRequest) = service.create(auth, request)

    @Post("/preview")
    fun preview(auth: Authentication, @Body request: FinderChangeRequest) = service.preview(auth, request)

    @Put("/{id}")
    fun update(auth: Authentication, id: Long, @Body request: FinderChangeRequest) = service.update(auth, id, request)

    @Delete("/{id}")
    fun delete(auth: Authentication, id: Long) = service.delete(auth, id)

    @Get("/{id}/open")
    fun getOpen(auth: Authentication, id: Long, pageable: Pageable) = service.getOpen(auth, id, pageable)

    @Get("/{id}/entry")
    fun getEntries(auth: Authentication, id: Long, @QueryValue answer: FinderAnswer, pageable: Pageable) =
        service.getEntries(auth, id, answer, pageable)

    @Put("/{id}/entry/{eventId}")
    fun setAnswer(auth: Authentication, id: Long, eventId: Long, @Body request: FinderAnswerRequest) =
        service.setAnswer(auth, id, eventId, request)

    @Delete("/{id}/entry/{eventId}")
    fun clearAnswer(auth: Authentication, id: Long, eventId: Long) = service.clearAnswer(auth, id, eventId)

    @Delete("/{id}/entry")
    fun resetAnswers(auth: Authentication, id: Long) = service.resetAnswers(auth, id)
}
