package de.sambalmueslie.openevent.gateway.portal.image

import de.sambalmueslie.openevent.core.image.api.UploadResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.http.multipart.CompletedFileUpload
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/portal/image")
@Tag(name = "APP Image API")
class ImageController(private val service: ImageGuardService) {

    @Post("/event/{eventId}/banner")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    fun uploadBannerImage(auth: Authentication, eventId: Long, @Part("image") file: CompletedFileUpload): HttpResponse<UploadResponse> =
        service.uploadBannerImage(auth, eventId, file)

    @Get("/event/{eventId}/banner")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun getImage(eventId: Long, auth: Authentication?) = service.getImage(eventId, auth)
}
