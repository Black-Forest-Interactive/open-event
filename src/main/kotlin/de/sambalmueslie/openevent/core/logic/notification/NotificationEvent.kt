package de.sambalmueslie.openevent.core.logic.notification


import de.sambalmueslie.openevent.core.model.Account
import de.sambalmueslie.openevent.infrastructure.mail.api.Attachment

data class NotificationEvent<T>(
    val key: String,
    val actor: Account,
    val content: T,
    val useActorAsSender: Boolean = false,
    val attachments: List<Attachment> = emptyList()
)
