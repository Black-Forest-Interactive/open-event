package de.sambalmueslie.openevent.core.notification.handler


import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.notification.NotificationTypeCrudService
import io.micronaut.context.annotation.Context
import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.context.event.StartupEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Context
class NotificationHandlerService(
    private val handler: List<NotificationHandler>,
    private val service: NotificationTypeCrudService,
    private val accountService: AccountCrudService
) : ApplicationEventListener<StartupEvent> {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(NotificationHandlerService::class.java)
    }

    override fun onApplicationEvent(event: StartupEvent) {
        val actor = accountService.getSystemAccount()
        handler.forEach { init(actor, it) }
    }

    private fun init(actor: Account, handler: NotificationHandler) {
        val types = handler.getTypes()
        if (types.isEmpty()) return

        val keys = types.map { it.key }.toSet()
        val existing = service.findByKeys(keys).associateBy { it.key }

        val toAdd = types.filter { !existing.containsKey(it.key) }
        toAdd.forEach { service.create(actor, it) }
    }


}
