package de.sambalmueslie.openevent.gateway.portal.finder

import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.finder.FinderCrudService
import de.sambalmueslie.openevent.core.finder.api.*
import de.sambalmueslie.openevent.core.search.api.EventSearchEntry
import de.sambalmueslie.openevent.error.IllegalAccessException
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import de.sambalmueslie.openevent.infrastructure.audit.api.AuditAction
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton

@Singleton
class FinderGuardService(
    private val service: FinderCrudService,
    private val accountService: AccountCrudService,
    audit: AuditService,
) {
    companion object {
        // finder is a personal view on events, so it shares the event read permission
        private const val PERMISSION_READ = "event.read"
    }

    private val logger = audit.getLogger("APP Finder API")

    fun get(auth: Authentication, pageable: Pageable): Page<FinderInfo> =
        auth.checkPermission(PERMISSION_READ) {
            val account = accountService.get(auth) ?: return@checkPermission Page.empty()
            service.getInfos(account, pageable)
        }

    fun get(auth: Authentication, id: Long): FinderInfo? =
        auth.checkPermission(PERMISSION_READ) {
            val (finder, account) = getOwned(auth, id) ?: return@checkPermission null
            service.getInfo(account, finder)
        }

    fun create(auth: Authentication, request: FinderChangeRequest) =
        auth.checkPermission(PERMISSION_READ) {
            logger.traceCreate(auth, request) {
                val account = accountService.find(auth)
                service.create(account, request)
            }
        }

    fun preview(auth: Authentication, request: FinderChangeRequest): FinderPreview =
        auth.checkPermission(PERMISSION_READ) {
            service.preview(accountService.find(auth), request)
        }

    fun update(auth: Authentication, id: Long, request: FinderChangeRequest) =
        auth.checkPermission(PERMISSION_READ) {
            val (_, account) = getOwned(auth, id) ?: return@checkPermission null
            logger.traceUpdate(auth, request) { service.update(account, id, request) }
        }

    fun delete(auth: Authentication, id: Long) =
        auth.checkPermission(PERMISSION_READ) {
            val (_, account) = getOwned(auth, id) ?: return@checkPermission null
            logger.traceDelete(auth) { service.delete(account, id) }
        }

    fun getOpen(auth: Authentication, id: Long, pageable: Pageable): Page<EventSearchEntry> =
        auth.checkPermission(PERMISSION_READ) {
            val (finder, account) = getOwned(auth, id) ?: return@checkPermission Page.empty()
            service.getOpen(account, finder, pageable)
        }

    fun getEntries(auth: Authentication, id: Long, answer: FinderAnswer, pageable: Pageable): Page<FinderEntryInfo> =
        auth.checkPermission(PERMISSION_READ) {
            val (finder, account) = getOwned(auth, id) ?: return@checkPermission Page.empty()
            service.getEntries(account, finder, answer, pageable)
        }

    fun setAnswer(auth: Authentication, id: Long, eventId: Long, request: FinderAnswerRequest) =
        auth.checkPermission(PERMISSION_READ) {
            val (finder, account) = getOwned(auth, id) ?: return@checkPermission null
            logger.traceAction(auth, AuditAction.FINDER_ANSWER_CHANGED, "$id/$eventId", request) {
                service.setAnswer(account, finder, eventId, request.answer)
            }
        }

    fun clearAnswer(auth: Authentication, id: Long, eventId: Long) =
        auth.checkPermission(PERMISSION_READ) {
            val (finder, account) = getOwned(auth, id) ?: return@checkPermission null
            logger.traceAction(auth, AuditAction.FINDER_ANSWER_REMOVED, "$id/$eventId") {
                service.clearAnswer(account, finder, eventId)
            }
        }

    fun resetAnswers(auth: Authentication, id: Long) =
        auth.checkPermission(PERMISSION_READ) {
            val (finder, account) = getOwned(auth, id) ?: return@checkPermission null
            logger.traceAction(auth, AuditAction.FINDER_RESET, "$id") { service.resetAnswers(account, finder) }
        }

    private fun getOwned(auth: Authentication, id: Long): Pair<Finder, Account>? {
        val account = accountService.find(auth)
        val finder = service.get(id) ?: return null
        if (finder.ownerId != account.id) throw IllegalAccessException("Cannot access finder cause user is not owner")
        return Pair(finder, account)
    }
}
