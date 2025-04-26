package de.sambalmueslie.openevent.gateway.external.registration

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class ParticipantConfirmRequest(
    val hash: String
)
