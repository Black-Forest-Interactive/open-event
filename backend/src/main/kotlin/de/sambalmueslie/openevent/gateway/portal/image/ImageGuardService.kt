package de.sambalmueslie.openevent.gateway.portal.image

import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.image.ImageCrudService
import de.sambalmueslie.openevent.core.image.api.UploadResponse
import de.sambalmueslie.openevent.gateway.portal.event.EventGuardService
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.http.HttpResponse
import io.micronaut.http.multipart.CompletedFileUpload
import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton
import kotlin.jvm.optionals.getOrNull

@Singleton
class ImageGuardService(
    private val service: ImageCrudService,
    private val eventService: EventGuardService,
    audit: AuditService,
) {

    companion object {
        private const val PERMISSION_READ = "image.read"
        private const val PERMISSION_WRITE = "image.write"
    }

    private val logger = audit.getLogger("APP Image API")

    fun uploadBannerImage(auth: Authentication, eventId: Long, file: CompletedFileUpload): HttpResponse<UploadResponse> =
        auth.checkPermission(PERMISSION_WRITE) {
            val (event, account) = eventService.getIfAccessible(auth, eventId) ?: return@checkPermission HttpResponse.notAllowed()
            val response = service.uploadBanner(account, event, file.filename, file.bytes, file.contentType.getOrNull())
            HttpResponse.ok(response)
        }

    fun getImage(eventId: Long, auth: Authentication?): HttpResponse<*> {
        eventService.getReadable(auth, eventId) ?: return HttpResponse.notFound("")
        val banner = service.getBanner(eventId) ?: return HttpResponse.notFound("")
        return HttpResponse.ok(banner.content)
            .header("Content-Type", banner.contentType)
            .header("Cache-Control", "public, max-age=31536000")
    }
}
