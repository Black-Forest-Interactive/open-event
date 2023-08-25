package de.sambalmueslie.openevent.core.storage

import de.sambalmueslie.openevent.core.Storage
import de.sambalmueslie.openevent.core.model.*
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable

interface NotificationSchemeStorage : Storage<Long, NotificationScheme, NotificationSchemeChangeRequest> {
    fun setEnabled(id: Long, value: PatchRequest<Boolean>): NotificationScheme?

    fun assign(scheme: NotificationScheme, types: List<NotificationType>)
    fun getByType(type: NotificationType, pageable: Pageable): Page<NotificationScheme>
    fun getSubscriber(scheme: NotificationScheme, pageable: Pageable): Page<Account>
    fun getSubscriptionStatus(account: Account): SubscriptionStatus
    fun subscribe(scheme: NotificationScheme, account: Account): SubscriptionStatus
    fun unsubscribe(scheme: NotificationScheme, account: Account): SubscriptionStatus

}
