package de.sambalmueslie.openevent.core.image

import de.sambalmueslie.openevent.config.ImageConfig
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.event.EventCrudService
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.image.api.UploadResponse
import io.micronaut.http.MediaType
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.io.ByteArrayInputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*
import kotlin.io.path.name


@Singleton
class ImageCrudService(
    private val validator: FileUploadValidator,
    private val eventService: EventCrudService,
    private val config: ImageConfig
) {

    companion object {
        private val logger = LoggerFactory.getLogger(ImageCrudService::class.java)
    }

    init {
        try {
            Files.createDirectories(Paths.get(config.uploadPath))
        } catch (e: IOException) {
            throw RuntimeException("Failed to create upload directory", e)
        }
    }


    fun uploadBanner(account: Account, event: Event, filename: String, bytes: ByteArray, contentType: MediaType?): UploadResponse {
        try {
            val fileExtension = getFileExtension(filename)
            validator.validate(fileExtension, bytes, contentType)

            val filePath = Paths.get(config.uploadPath, "${event.id}", "banner$fileExtension")
            Files.copy(ByteArrayInputStream(bytes), filePath, StandardCopyOption.REPLACE_EXISTING)
            logger.debug("[${account.id}] file uploaded successfully: {}", filePath)
            return UploadResponse(true, filename, "", "")
        } catch (e: Throwable) {
            logger.error("[${account.id}] banner upload failed", e)
            return UploadResponse(false, "", "", e.message)
        }
    }

    private fun getFileExtension(filename: String): String {
        val lastDotIndex = filename.lastIndexOf('.')
        return if (lastDotIndex == -1) "" else filename.substring(lastDotIndex).lowercase(Locale.getDefault())
    }


    fun getBanner(eventId: Long): ByteArray? {
        val filePath = Paths.get(config.uploadPath, "$eventId")
        val banner = Files.list(filePath).filter { it.fileName.name.contains("banner", true) }.findFirst().orElse(null) ?: return null
        return Files.readAllBytes(banner)
    }

}