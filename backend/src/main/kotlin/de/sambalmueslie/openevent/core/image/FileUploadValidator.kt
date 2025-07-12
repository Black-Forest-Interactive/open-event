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