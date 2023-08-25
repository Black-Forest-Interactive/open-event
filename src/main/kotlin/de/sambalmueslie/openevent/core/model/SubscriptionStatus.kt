package de.sambalmueslie.openevent.core.model

data class SubscriptionStatus(
    val unsubscribed: List<NotificationScheme>,
    val subscribed: List<NotificationScheme>,
)
