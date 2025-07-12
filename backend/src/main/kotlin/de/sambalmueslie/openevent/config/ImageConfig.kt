package de.sambalmueslie.openevent.config

import io.micronaut.context.annotation.ConfigurationProperties
import jakarta.validation.constraints.NotBlank
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@ConfigurationProperties("img")
class ImageConfig {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ImageConfig::class.java)
    }

    @NotBlank
    var uploadPath: String = "/opt/upload"
        set(value) {
            logger.info("Set image upload path from '$field' to '$value'")
            field = value
        }

    @NotBlank
    var maxFileSize: String = "1MB"
        set(value) {
            logger.info("Set max file size from '$field' to '$value'")
            field = value
        }

    var allowedMimeTypes = setOf("image/jpeg", "image/png", "image/gif", "image/webp")
        set(value) {
            logger.info("Set allowed mime types from '$field' to '$value'")
            field = value
        }

    var allowedExtensions = setOf(".jpg", ".jpeg", ".png", ".gif", ".webp")
        set(value) {
            logger.info("Set allowed extensions from '$field' to '$value'")
            field = value
        }
}