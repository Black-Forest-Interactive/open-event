package de.sambalmueslie.openevent.api

import de.sambalmueslie.openevent.core.model.SubscriptionStatus
import io.micronaut.security.authentication.Authentication

interface SubscriptionAPI {
    companion object {
        const val PERMISSION_READ = "openevent.subscription.read"
        const val PERMISSION_WRITE = "openevent.subscription.write"
        const val PERMISSION_ADMIN = "openevent.subscription.admin"
    }
    fun subscribe(auth: Authentication, schemeId: Long): SubscriptionStatus
    fun unsubscribe(auth: Authentication, schemeId: Long): SubscriptionStatus
    fun getStatus(auth: Authentication): SubscriptionStatus
}
