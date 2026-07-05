package de.sambalmueslie.openevent.gateway.backoffice.feedback

import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.feedback.FeedbackCrudService
import de.sambalmueslie.openevent.core.feedback.api.Feedback
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton

@Singleton
class FeedbackGuardService(
    private val service: FeedbackCrudService,
    private val accountService: AccountCrudService,
    audit: AuditService,
) {
    companion object {
        private const val PERMISSION_ADMIN = "feedback.admin"
    }

    private val logger = audit.getLogger("BACKOFFICE Feedback API")

    fun getAll(auth: Authentication, pageable: Pageable) = auth.checkPermission(PERMISSION_ADMIN) { service.getAll(pageable) }

    fun get(auth: Authentication, id: Long) = auth.checkPermission(PERMISSION_ADMIN) { service.get(id) }

    fun getByTopic(auth: Authentication, topic: String, pageable: Pageable) =
        auth.checkPermission(PERMISSION_ADMIN) { service.getByTopic(topic, pageable) }

    fun getByAccount(auth: Authentication, accountId: Long, pageable: Pageable): Page<Feedback> =
        auth.checkPermission(PERMISSION_ADMIN) {
            val account = accountService.get(accountId) ?: return@checkPermission Page.empty()
            service.getByAccount(account, pageable)
        }
}
