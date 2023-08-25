package de.sambalmueslie.openevent.core.logic.notification


import de.sambalmueslie.openevent.core.BaseCrudService
import de.sambalmueslie.openevent.core.model.*
import de.sambalmueslie.openevent.core.storage.NotificationSchemeStorage
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


    fun setEnabled(id: Long, value: PatchRequest<Boolean>): NotificationScheme? {
        return storage.setEnabled(id, value)
    }

    fun getByType(type: NotificationType, pageable: Pageable): Page<NotificationScheme> {
        return storage.getByType(type, pageable)
    }

    fun getSubscriber(scheme: NotificationScheme, pageable: Pageable): Page<Account> {
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
