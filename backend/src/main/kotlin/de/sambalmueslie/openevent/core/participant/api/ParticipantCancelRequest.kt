package de.sambalmueslie.openevent.core.participant.api

import de.sambalmueslie.openevent.core.notification.SafeHtml
import org.jsoup.nodes.Entities

/**
 * Optional message a participant can send to the organizer when canceling the participation.
 */
data class ParticipantCancelRequest(
    val message: String = "",
) {
    companion object {
        const val MAX_MESSAGE_LENGTH = 1000
    }

    fun hasContent() = message.isNotBlank()

    /** HTML escaped message, line breaks converted to <br> - safe for use in mail templates. */
    fun formatMessage() = SafeHtml(Entities.escape(message.trim()).replace("\n", "<br>"))
}
