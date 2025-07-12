package de.sambalmueslie.openevent.core.account


import de.sambalmueslie.openevent.common.BaseCrudService
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.api.Profile
import de.sambalmueslie.openevent.core.account.api.ProfileChangeRequest
import de.sambalmueslie.openevent.infrastructure.settings.SettingsService
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class ProfileCrudService(
    private val storage: ProfileStorage,
    private val settings: SettingsService,
) : BaseCrudService<Long, Profile, ProfileChangeRequest, ProfileChangeListener>(storage) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ProfileCrudService::class.java)
    }

    fun create(actor: Account, account: Account, request: ProfileChangeRequest): Profile {
        val result = storage.create(request, account)
        notifyCreated(actor, result)
        return result
    }

    fun getForAccount(account: Account): Profile? {
        return storage.findByAccount(account)
    }

    fun getForAccounts(accounts: Collection<Account>): List<Profile> {
        return storage.getForAccounts(accounts)
    }

    fun handleAccountCreated(actor: Account, account: Account) {
        val existing = storage.findByAccount(account)
        if (existing != null) return

        val request = ProfileChangeRequest(
            null,
            null,
            null,
            "",
            "",
            null,
            null,
            null,
            null,
            settings.getLanguage()
        )

        create(actor, account, request)
    }

    fun merge(actor: Account, account: Account, request: ProfileChangeRequest): Profile {
        val profile = storage.findByAccount(account)

        val profileRequest = ProfileChangeRequest(
            profile?.email ?: request.email,
            profile?.phone ?: request.phone,
            profile?.mobile ?: request.mobile,
            profile?.firstName.isNullOrBlank().let { request.firstName },
            profile?.lastName.isNullOrBlank().let { request.lastName },
            profile?.dateOfBirth ?: request.dateOfBirth,
            profile?.gender ?: request.gender,
            profile?.profilePicture ?: request.profilePicture,
            profile?.website ?: request.website,
            (profile?.language ?: request.language).ifBlank { settings.getLanguage() }
        )

        return if (profile == null) {
            create(account, account, profileRequest)
        } else {
            update(account, profile.id, profileRequest)
        }
    }

    override fun isValid(request: ProfileChangeRequest) {
        // intentionally left empty
    }


}
