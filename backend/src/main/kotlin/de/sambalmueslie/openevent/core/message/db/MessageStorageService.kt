package de.sambalmueslie.openevent.core.message.db


import de.sambalmueslie.openevent.common.BaseStorageService
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.message.api.Message
import de.sambalmueslie.openevent.core.message.api.MessageChangeRequest
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.infrastructure.cache.CacheService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
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



}
