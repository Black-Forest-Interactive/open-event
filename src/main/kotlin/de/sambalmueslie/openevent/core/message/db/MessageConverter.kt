package de.sambalmueslie.openevent.core.message.db


import de.sambalmueslie.openevent.common.DataObjectConverter
import de.sambalmueslie.openevent.core.logic.account.api.Account
import de.sambalmueslie.openevent.core.logic.account.db.AccountStorageService
import de.sambalmueslie.openevent.core.message.api.Message
import de.sambalmueslie.openevent.error.InconsistentDataException
import io.micronaut.data.model.Page
import jakarta.inject.Singleton

@Singleton
class MessageConverter(
    private val accountService: AccountStorageService,
) : DataObjectConverter<Message, MessageData> {

    override fun convert(obj: MessageData): Message {
        return convert(obj, accountService.get(obj.authorId))
    }

    override fun convert(objs: List<MessageData>): List<Message> {
        val authorIds = objs.map { it.authorId }.toSet()
        val author = accountService.getByIds(authorIds).associateBy { it.id }
        return objs.map { convert(it, author[it.id]) }
    }

    override fun convert(page: Page<MessageData>): Page<Message> {
        val authorIds = page.content.map { it.authorId }.toSet()
        val author = accountService.getByIds(authorIds).associateBy { it.id }
        return page.map { convert(it, author[it.id]) }
    }

    private fun convert(data: MessageData, author: Account?): Message {
        if (author == null) throw InconsistentDataException("Cannot find author for announcement")
        return data.convert(author)
    }
}
