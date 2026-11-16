package de.sambalmueslie.openevent.core.image

import de.sambalmueslie.openevent.config.ImageConfig
import de.sambalmueslie.openevent.error.InvalidRequestException
import io.micronaut.http.MediaType
import jakarta.inject.Singleton
import java.util.*

@Singleton
class FileUploadValidator(
    private val config: ImageConfig
) {
    private val maxBytes: Long = parseSize(config.maxFileSize)

    fun validate(fileExtension: String, bytes: ByteArray, contentType: MediaType?) {
        if (bytes.size >= maxBytes) throw InvalidRequestException("File size exceeds maximum allowed size of " + config.maxFileSize)

        if (contentType == null || !config.allowedMimeTypes.contains(contentType.toString())) throw InvalidRequestException("Invalid file type. Content-Type: " + contentType)

        val extension = fileExtension.lowercase(Locale.getDefault())
        if (!config.allowedExtensions.contains(extension)) throw InvalidRequestException("File type not allowed. Allowed types: " + config.allowedExtensions)
        if (!isImage(bytes)) throw InvalidRequestException("File content is not a supported image")
    }

    /** check the magic bytes, cause extension and content type are provided by the client */
    private fun isImage(bytes: ByteArray): Boolean {
        fun startsWith(vararg signature: Int, offset: Int = 0) =
            bytes.size >= offset + signature.size && signature.indices.all { bytes[offset + it] == signature[it].toByte() }

        val jpeg = startsWith(0xFF, 0xD8, 0xFF)
        val png = startsWith(0x89, 0x50, 0x4E, 0x47)
        val gif = startsWith(0x47, 0x49, 0x46, 0x38)
        val webp = startsWith(0x52, 0x49, 0x46, 0x46) && startsWith(0x57, 0x45, 0x42, 0x50, offset = 8)
        return jpeg || png || gif || webp
    }


    private fun parseSize(sizeStr: String?): Long {
        var sizeStr = sizeStr
        if (sizeStr == null || sizeStr.isEmpty()) {
            return (10 * 1024 * 1024).toLong()
        }

        sizeStr = sizeStr.trim { it <= ' ' }.uppercase(Locale.getDefault())
        var multiplier: Long = 1

        if (sizeStr.endsWith("KB")) {
            multiplier = 1024
            sizeStr = sizeStr.substring(0, sizeStr.length - 2)
        } else if (sizeStr.endsWith("MB")) {
            multiplier = (1024 * 1024).toLong()
            sizeStr = sizeStr.substring(0, sizeStr.length - 2)
        } else if (sizeStr.endsWith("GB")) {
            multiplier = (1024 * 1024 * 1024).toLong()
            sizeStr = sizeStr.substring(0, sizeStr.length - 2)
        }

        return try {
            sizeStr.trim { it <= ' ' }.toLong() * multiplier
        } catch (e: NumberFormatException) {
            (10 * 1024 * 1024).toLong()
        }
    }

}