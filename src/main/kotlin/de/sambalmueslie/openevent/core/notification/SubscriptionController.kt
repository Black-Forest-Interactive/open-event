package de.sambalmueslie.openevent.core.notification


import de.sambalmueslie.openevent.api.SubscriptionAPI
import de.sambalmueslie.openevent.api.SubscriptionAPI.Companion.PERMISSION_ADMIN
import de.sambalmueslie.openevent.api.SubscriptionAPI.Companion.PERMISSION_READ
import de.sambalmueslie.openevent.api.SubscriptionAPI.Companion.PERMISSION_WRITE
import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.notification.api.SubscriptionStatus
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Put
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/subscription")
@Tag(name = "Subscription API")
class SubscriptionController(
    private val schemeService: NotificationSchemeCrudService,
    private val accountService: AccountCrudService,
    audit: AuditService,
) : SubscriptionAPI {
    private val logger = audit.getLogger("Subscription API")

    @Put("/{schemeId}")
    override fun subscribe(auth: Authentication, schemeId: Long): SubscriptionStatus {
        return auth.checkPermission(PERMISSION_WRITE, PERMISSION_ADMIN) {
            logger.traceAction(auth, "SUBSCRIBE", schemeId.toString()) {
                schemeService.subscribe(accountService.find(auth), schemeId)
            } ?: SubscriptionStatus(emptyList(), emptyList())
        }
    }

    @Delete("/{schemeId}")
    override fun unsubscribe(auth: Authentication, schemeId: Long): SubscriptionStatus {
        return auth.checkPermission(PERMISSION_WRITE, PERMISSION_ADMIN) {
            logger.traceAction(auth, "UNSUBSCRIBE", schemeId.toString()) {
                schemeService.unsubscribe(accountService.find(auth), schemeId)
            } ?: SubscriptionStatus(emptyList(), emptyList())
        }
    }

    @Get()
    override fun getStatus(auth: Authentication): SubscriptionStatus {
        return auth.checkPermission(PERMISSION_READ, PERMISSION_ADMIN) {
            schemeService.getSubscriptionStatus(accountService.find(auth))
        }
    }


}
