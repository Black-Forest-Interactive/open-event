package de.sambalmueslie.openevent.core.message.db

import de.sambalmueslie.openevent.common.DataObject
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.message.api.Message
import de.sambalmueslie.openevent.core.message.api.MessageChangeRequest
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "Message")
@Table(name = "message")
data class MessageData(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long = 0,
    @Column var authorId: Long = 0,

    @Column var subject: String,
    @Column var content: String,

    @Column var created: LocalDateTime = LocalDateTime.now(),
    @Column var updated: LocalDateTime? = null
) : DataObject {

    companion object {
        fun create(
            account: Account,
            request: MessageChangeRequest,
            timestamp: LocalDateTime
        ): MessageData {
            return MessageData(0, account.id, request.subject, request.content, timestamp)
        }
    }

    fun convert(account: Account): Message {
        return Message(id, subject, content, created, account)
    }

    fun update(request: MessageChangeRequest, timestamp: LocalDateTime): MessageData {
        subject = request.subject
        content = request.content
        updated = timestamp
        return this
    }
}
