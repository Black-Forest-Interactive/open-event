package de.sambalmueslie.openevent.core.account


import de.sambalmueslie.openevent.common.BaseCrudService
import de.sambalmueslie.openevent.core.account.api.*
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class PreferencesCrudService(
    private val storage: PreferencesStorage
) : BaseCrudService<Long, Preferences, PreferencesChangeRequest, PreferencesChangeListener>(storage) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(PreferencesCrudService::class.java)
    }

    fun create(actor: Account, account: Account, request: PreferencesChangeRequest): Preferences {
        val result = storage.create(request, account)
        notifyCreated(actor, result)
        return result
    }

    fun getForAccount(account: Account): Preferences? {
        return storage.findByAccount(account)
    }

    fun handleAccountCreated(actor: Account, account: Account) {
        val existing = storage.findByAccount(account)
        if (existing != null) return

        val request = PreferencesChangeRequest(
            EmailNotificationsPreferences(),
            CommunicationPreferences(),
            NotificationPreferences()
        )
        create(actor, account, request)
    }

    override fun isValid(request: PreferencesChangeRequest) {
        // intentionally left empty
    }
}
