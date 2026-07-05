package de.sambalmueslie.openevent.gateway.app.account

import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.account.PreferencesCrudService
import de.sambalmueslie.openevent.core.account.ProfileCrudService
import de.sambalmueslie.openevent.core.account.api.*
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton

@Singleton
class AccountGuardService(
    private val service: AccountCrudService,
    private val profileService: ProfileCrudService,
    private val preferencesService: PreferencesCrudService,
    audit: AuditService,
) {
    companion object {
        private const val PERMISSION_READ = "account.read"
        private const val PERMISSION_WRITE = "account.write"
    }

    private val logger = audit.getLogger("APP Account API")

    fun get(auth: Authentication) = auth.checkPermission(PERMISSION_READ) { service.get(auth) }

    fun update(auth: Authentication, request: AccountChangeRequest) =
        auth.checkPermission(PERMISSION_WRITE) {
            val account = service.get(auth)
            if (account == null) {
                val systemAccount = service.getSystemAccount()
                logger.traceCreate(auth, request) { service.create(systemAccount, request) }
            } else {
                logger.traceUpdate(auth, request) {
                    service.update(service.find(auth), account.id, request)
                }
            }
        }

    fun validate(auth: Authentication, lang: String) = auth.checkPermission(PERMISSION_READ) { service.validate(auth, lang) }

    fun getProfile(auth: Authentication) =
        auth.checkPermission(PERMISSION_READ) {
            val account = service.get(auth) ?: return@checkPermission null
            profileService.getForAccount(account)
        }

    fun updateProfile(auth: Authentication, request: ProfileChangeRequest) =
        auth.checkPermission(PERMISSION_READ) {
            val account = service.get(auth) ?: return@checkPermission null
            profileService.update(account, account.id, request)
        }

    fun getPreferences(auth: Authentication) =
        auth.checkPermission(PERMISSION_READ) {
            val account = service.get(auth) ?: return@checkPermission null
            preferencesService.getForAccount(account)
        }

    fun updatePreferences(auth: Authentication, request: PreferencesChangeRequest) =
        auth.checkPermission(PERMISSION_READ) {
            val account = service.get(auth) ?: return@checkPermission null
            preferencesService.update(account, account.id, request)
        }
}
