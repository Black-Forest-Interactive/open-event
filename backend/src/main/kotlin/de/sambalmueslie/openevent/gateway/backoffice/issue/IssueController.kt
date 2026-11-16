package de.sambalmueslie.openevent.gateway.backoffice.issue

import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.core.issue.api.IssueStatus
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Put
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/backoffice/issue")
@Tag(name = "BACKOFFICE Issue API")
class IssueController(private val service: IssueGuardService) {

    @Get("/{id}")
    fun get(auth: Authentication, id: Long) = service.get(auth, id)

    @Get()
    fun getAll(auth: Authentication, pageable: Pageable) = service.getAll(auth, pageable)

    @Get("account/{accountId}")
    fun getByAccount(auth: Authentication, accountId: Long, pageable: Pageable) = service.getByAccount(auth, accountId, pageable)

    @Get("account/{accountId}/unresolved")
    fun getUnresolvedByAccount(auth: Authentication, accountId: Long, pageable: Pageable) = service.getUnresolvedByAccount(auth, accountId, pageable)

    @Get("unresolved")
    fun getUnresolved(auth: Authentication, pageable: Pageable) = service.getUnresolved(auth, pageable)

    @Put("/{id}/status")
    fun changeStatus(auth: Authentication, id: Long, @Body status: PatchRequest<IssueStatus>) = service.changeStatus(auth, id, status)
}
