package de.sambalmueslie.openevent.infrastructure.mail.api

data class Attachment(
    val name: String,
    val data: ByteArray,
    val mimeType: String
)
