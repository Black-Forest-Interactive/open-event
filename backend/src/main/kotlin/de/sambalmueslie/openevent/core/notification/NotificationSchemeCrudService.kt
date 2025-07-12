package de.sambalmueslie.openevent.core.notification


import de.sambalmueslie.openevent.common.BaseCrudService
import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.api.AccountInfo
import de.sambalmueslie.openevent.core.notification.api.NotificationScheme
import de.sambalmueslie.openevent.core.notification.api.NotificationSchemeChangeRequest
import de.sambalmueslie.openevent.core.notification.api.NotificationType
import de.sambalmueslie.openevent.core.notification.api.SubscriptionStatus
import de.sambalmueslie.openevent.core.notification.db.NotificationSchemeStorage
import de.sambalmueslie.openevent.error.InvalidRequestException
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class NotificationSchemeCrudService(
    private val storage: NotificationSchemeStorage,
) : BaseCrudService<Long, NotificationScheme, NotificationSchemeChangeRequest, NotificationSchemeChangeListener>(storage) {


    companion object {
        private val logger: Logger = LoggerFactory.getLogger(NotificationSchemeCrudService::class.java)
    }

    override fun isValid(request: NotificationSchemeChangeRequest) {
        if (request.name.isBlank()) throw InvalidRequestException("Name cannot be blank.")
    }

    fun setEnabled(id: Long, value: PatchRequest<Boolean>): NotificationScheme? {
        return storage.setEnabled(id, value)
    }

    fun getByType(type: NotificationType, pageable: Pageable): Page<NotificationScheme> {
        return storage.getByType(type, pageable)
    }

    fun getSubscriber(scheme: NotificationScheme, pageable: Pageable): Page<AccountInfo> {
        return storage.getSubscriber(scheme, pageable)
    }

    fun getSubscriptionStatus(account: Account): SubscriptionStatus {
        return storage.getSubscriptionStatus(account)
    }

    fun subscribe(account: Account, schemeId: Long): SubscriptionStatus {
        val scheme = get(schemeId) ?: return getSubscriptionStatus(account)
        return storage.subscribe(scheme, account)
    }


    fun unsubscribe(account: Account, schemeId: Long): SubscriptionStatus {
        val scheme = get(schemeId) ?: return getSubscriptionStatus(account)
        return storage.unsubscribe(scheme, account)
    }
}
