package de.sambalmueslie.openevent.gateway.backoffice.feedback

import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.feedback.FeedbackCrudService
import de.sambalmueslie.openevent.core.feedback.api.Feedback
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/backoffice/feedback")
@Tag(name = "BACKOFFICE Feedback API")
class FeedbackController(
    private val service: FeedbackCrudService,
    private val accountService: AccountCrudService,
    audit: AuditService,
) {
    companion object {
        private const val PERMISSION_ADMIN = "feedback.admin"
    }

    private val logger = audit.getLogger("BACKOFFICE Feedback API")

    @Get("/by/topic/{topic}")
    fun getByTopic(auth: Authentication, topic: String, pageable: Pageable): Page<Feedback> {
        return auth.checkPermission(PERMISSION_ADMIN) {
            service.getByTopic(topic, pageable)
        }
    }
    @Get("/by/account/{accountId}")
    fun getByAccount(auth: Authentication, accountId: Long, pageable: Pageable): Page<Feedback> {
        return auth.checkPermission(PERMISSION_ADMIN) {
            val account = accountService.get(accountId) ?: return@checkPermission Page.empty()
            service.getByAccount(account, pageable)
        }
    }
}