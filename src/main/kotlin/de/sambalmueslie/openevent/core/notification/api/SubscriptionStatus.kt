package de.sambalmueslie.openevent.core.notification.api

data class SubscriptionStatus(
    val unsubscribed: List<NotificationScheme>,
    val subscribed: List<NotificationScheme>,
)
