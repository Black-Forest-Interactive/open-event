package de.sambalmueslie.openevent.gateway.app.image

import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.image.ImageCrudService
import de.sambalmueslie.openevent.core.image.api.UploadResponse
import de.sambalmueslie.openevent.gateway.app.event.EventGuardService
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.http.multipart.CompletedFileUpload
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule
import io.swagger.v3.oas.annotations.tags.Tag
import kotlin.jvm.optionals.getOrNull


@Controller("/api/app/image")
@Tag(name = "APP Image API")
class ImageController(
    private val service: ImageCrudService,
    private val eventService: EventGuardService,
    audit: AuditService,
) {

    companion object {
        private const val PERMISSION_READ = "image.read"
        private const val PERMISSION_WRITE = "image.write"
    }

    private val logger = audit.getLogger("APP Image API")

    @Post("/event/{eventId}/banner")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    fun uploadBannerImage(auth: Authentication, eventId: Long, @Part("image") file: CompletedFileUpload): HttpResponse<UploadResponse> {
        return auth.checkPermission(PERMISSION_WRITE) {
            val (event, account) = eventService.getIfAccessible(auth, eventId) ?: return@checkPermission HttpResponse.notAllowed()
            val response = service.uploadBanner(account, event, file.filename, file.bytes, file.contentType.getOrNull())
            HttpResponse.ok(response)
        }
    }


    @Get("/event/{eventId}/banner")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun getImage(eventId: Long): HttpResponse<*>? {
        val data = service.getBanner(eventId) ?: return HttpResponse.notFound("")
        return HttpResponse.ok(data)
            .header("Content-Type", "image/jpeg")
            .header("Cache-Control", "public, max-age=31536000")
    }
}