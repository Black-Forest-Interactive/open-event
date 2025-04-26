package de.sambalmueslie.openevent.core.share.api

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class SharedAccount(
    val id: Long,
    val name: String,
)
