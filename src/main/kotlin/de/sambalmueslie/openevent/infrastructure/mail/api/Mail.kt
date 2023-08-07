package de.sambalmueslie.openevent.infrastructure.mail.api

data class Mail(
    val subject: String,
    val htmlText: String?,
    val plainText: String?,
    val images: MutableList<Image> = mutableListOf()
)
