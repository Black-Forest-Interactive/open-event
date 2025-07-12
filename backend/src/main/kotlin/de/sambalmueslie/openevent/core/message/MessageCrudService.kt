package de.sambalmueslie.openevent.core.message

import de.sambalmueslie.openevent.common.BaseCrudService
import de.sambalmueslie.openevent.core.message.api.Message
import de.sambalmueslie.openevent.core.message.api.MessageChangeRequest
import de.sambalmueslie.openevent.core.message.db.MessageStorage
import de.sambalmueslie.openevent.error.InvalidRequestException
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class MessageCrudService(
    private val storage: MessageStorage,
) : BaseCrudService<Long, Message, MessageChangeRequest, MessageChangeListener>(storage) {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(MessageCrudService::class.java)
    }

    override fun isValid(request: MessageChangeRequest) {
        if (request.subject.isBlank()) throw InvalidRequestException("Subject cannot be blank")
    }
}