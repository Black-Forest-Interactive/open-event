package de.sambalmueslie.openevent.core.image

import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.image.api.UploadResponse
import jakarta.inject.Singleton

@Singleton
class ImageCrudService {

    private val data = mutableMapOf<Long, ByteArray>()

    fun uploadBanner(account: Account, event: Event, filename: String, bytes: ByteArray): UploadResponse {
//        TODO("Not yet implemented")
        data.put(event.id, bytes)
        return UploadResponse(true, filename, "", "")
    }

    fun getBanner(eventId: Long): ByteArray? {
        return data[eventId]
    }

    fun getContentType(filename: String): String {
        val extension: String = getFileExtension(filename).lowercase()
        return when (extension) {
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "gif" -> "image/gif"
            "webp" -> "image/webp"
            else -> "application/octet-stream"
        }
    }

    private fun getFileExtension(filename: String): String {
        if (filename.isBlank()) {
            return ""
        }
        val lastDotIndex = filename.lastIndexOf('.')
        return if (lastDotIndex > 0) filename.substring(lastDotIndex + 1) else ""
    }
}