package de.sambalmueslie.openevent.core.message.db

import de.sambalmueslie.openevent.common.Storage
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.message.api.Message
import de.sambalmueslie.openevent.core.message.api.MessageChangeRequest

interface MessageStorage : Storage<Long, Message, MessageChangeRequest> {
    fun create(request: MessageChangeRequest, author: Account): Message
}
