package de.sambalmueslie.openevent.core.image

import de.sambalmueslie.openevent.config.ImageConfig
import de.sambalmueslie.openevent.error.InvalidRequestException
import io.micronaut.http.MediaType
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class FileUploadValidatorTest {

    private val validator = FileUploadValidator(ImageConfig())
    private fun bytes(vararg values: Int) = values.map { it.toByte() }.toByteArray() + ByteArray(16)

    @Test
    fun acceptsRealImages() {
        assertDoesNotThrow { validator.validate(".png", bytes(0x89, 0x50, 0x4E, 0x47), MediaType.IMAGE_PNG_TYPE) }
        assertDoesNotThrow { validator.validate(".jpg", bytes(0xFF, 0xD8, 0xFF, 0xE0), MediaType.IMAGE_JPEG_TYPE) }
        assertDoesNotThrow { validator.validate(".webp", "RIFFxxxxWEBP".toByteArray(), MediaType.of("image/webp")) }
    }

    @Test
    fun rejectsDisguisedContent() {
        val html = "<html><script>alert(1)</script></html>".toByteArray()
        assertThrows(InvalidRequestException::class.java) { validator.validate(".png", html, MediaType.IMAGE_PNG_TYPE) }
        assertThrows(InvalidRequestException::class.java) { validator.validate(".svg", html, MediaType.IMAGE_PNG_TYPE) }
        assertThrows(InvalidRequestException::class.java) { validator.validate(".png", bytes(0x89, 0x50, 0x4E, 0x47), MediaType.TEXT_HTML_TYPE) }
    }
}
