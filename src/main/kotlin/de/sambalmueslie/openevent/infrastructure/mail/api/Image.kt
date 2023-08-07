package de.sambalmueslie.openevent.infrastructure.mail.api

data class Image(
    val name: String,
    val data: ByteArray,
    val mimeType: String
)
