package de.sambalmueslie.openevent.core.image.api

data class UploadResponse(
    val success: Boolean,
    val imageUrl: String?,
    val message: String?,
    val error: String?
)
