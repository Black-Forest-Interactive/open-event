package de.sambalmueslie.openevent.core.notification.db

import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.common.Storage
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.api.AccountInfo
import de.sambalmueslie.openevent.core.notification.api.NotificationScheme
import de.sambalmueslie.openevent.core.notification.api.NotificationSchemeChangeRequest
import de.sambalmueslie.openevent.core.notification.api.NotificationType
import de.sambalmueslie.openevent.core.notification.api.SubscriptionStatus
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable

interface NotificationSchemeStorage : Storage<Long, NotificationScheme, NotificationSchemeChangeRequest> {
    fun setEnabled(id: Long, value: PatchRequest<Boolean>): NotificationScheme?

    fun assign(scheme: NotificationScheme, types: List<NotificationType>)
    fun getByType(type: NotificationType, pageable: Pageable): Page<NotificationScheme>
    fun getSubscriber(scheme: NotificationScheme, pageable: Pageable): Page<AccountInfo>
    fun getSubscriptionStatus(account: Account): SubscriptionStatus
    fun subscribe(scheme: NotificationScheme, account: Account): SubscriptionStatus
    fun unsubscribe(scheme: NotificationScheme, account: Account): SubscriptionStatus

}
