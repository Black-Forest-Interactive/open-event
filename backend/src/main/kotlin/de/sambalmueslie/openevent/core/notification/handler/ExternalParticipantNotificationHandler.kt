package de.sambalmueslie.openevent.core.notification.handler

import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.api.AccountInfo
import de.sambalmueslie.openevent.core.event.api.EventInfo
import de.sambalmueslie.openevent.core.notification.NotificationEvent
import de.sambalmueslie.openevent.core.notification.NotificationService
import de.sambalmueslie.openevent.core.notification.api.NotificationTypeChangeRequest
import de.sambalmueslie.openevent.core.registration.api.RegistrationInfo
import de.sambalmueslie.openevent.gateway.external.participant.db.ExternalParticipantData
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class ExternalParticipantNotificationHandler(
    private val service: NotificationService,
) : NotificationHandler {


    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ExternalParticipantNotificationHandler::class.java)
        const val KEY_EXTERNAL_PARTICIPANT_CREATED = "external.participant.create"
    }


    override fun getName(): String = ExternalParticipantNotificationHandler::class.java.simpleName

    override fun getTypes(): Set<NotificationTypeChangeRequest> {
        return setOf(
            NotificationTypeChangeRequest(KEY_EXTERNAL_PARTICIPANT_CREATED, "External participant created", ""),
        )
    }

    fun handleCreated(actor: Account, event: EventInfo, registration: RegistrationInfo, obj: ExternalParticipantData) {
        val content = ExternalParticipantEventContent(event.event, registration.registration, obj)
        service.process(
            NotificationEvent(
                KEY_EXTERNAL_PARTICIPANT_CREATED,
                actor,
                content
            ), getRecipients(actor, obj)
        )
    }

    private fun getRecipients(
        actor: Account,
        obj: ExternalParticipantData
    ): Collection<AccountInfo> {
        return listOf(obj.toAccountInfo())
    }
}

private fun ExternalParticipantData.toAccountInfo(): AccountInfo {
    val name = "$firstName $lastName"
    return AccountInfo(-1, name, "", email, firstName, lastName, language)
}
