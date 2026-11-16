package de.sambalmueslie.openevent.gateway.backoffice.notification

import de.sambalmueslie.openevent.common.PatchRequest
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Put
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/backoffice/notification")
@Tag(name = "BACKOFFICE Notification API")
class NotificationController(private val service: NotificationGuardService) {

    @Get("/type")
    fun getTypes(auth: Authentication, pageable: Pageable) = service.getTypes(auth, pageable)

    @Put("/type/{id}/enabled")
    fun setTypeEnabled(auth: Authentication, id: Long, @Body value: PatchRequest<Boolean>) = service.setTypeEnabled(auth, id, value)
}
