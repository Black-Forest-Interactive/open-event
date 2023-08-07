package de.sambalmueslie.openevent.storage.message


import de.sambalmueslie.openevent.core.model.Account
import de.sambalmueslie.openevent.core.model.Message
import de.sambalmueslie.openevent.core.model.MessageChangeRequest
import de.sambalmueslie.openevent.core.storage.MessageStorage
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.infrastructure.cache.CacheService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import de.sambalmueslie.openevent.storage.BaseStorageService
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class MessageStorageService(
    private val repository: MessageRepository,
    private val converter: MessageConverter,
    cacheService: CacheService,
    private val timeProvider: TimeProvider,
) : BaseStorageService<Long, Message, MessageChangeRequest, MessageData>(
    repository,
    converter,
    cacheService,
    Message::class,
    logger
), MessageStorage {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(MessageStorageService::class.java)
        private const val AUTHOR_REFERENCE = "author"
    }

    override fun create(request: MessageChangeRequest, author: Account): Message {
        return create(request, mapOf(Pair(AUTHOR_REFERENCE, author)))
    }

    override fun createData(request: MessageChangeRequest, properties: Map<String, Any>): MessageData {
        val account = properties[AUTHOR_REFERENCE] as? Account ?: throw InvalidRequestException("Cannot find account")
        return MessageData.create(account, request, timeProvider.now())
    }

    override fun updateData(data: MessageData, request: MessageChangeRequest): MessageData {
        return data.update(request, timeProvider.now())
    }

    override fun isValid(request: MessageChangeRequest) {
        if (request.subject.isBlank()) throw InvalidRequestException("Subject cannot be blank")
    }


}
