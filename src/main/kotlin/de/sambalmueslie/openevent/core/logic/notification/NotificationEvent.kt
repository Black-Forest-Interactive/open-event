package de.sambalmueslie.openevent.core.logic.notification


import de.sambalmueslie.openevent.core.model.Account

data class NotificationEvent<T>(
    val key: String,
    val actor: Account,
    val content: T,
    val useActorAsSender: Boolean = false
)
