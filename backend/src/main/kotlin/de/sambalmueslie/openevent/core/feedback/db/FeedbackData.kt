package de.sambalmueslie.openevent.core.feedback.db

import de.sambalmueslie.openevent.common.DataObject
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.feedback.api.Feedback
import de.sambalmueslie.openevent.core.feedback.api.FeedbackChangeRequest
import jakarta.persistence.*
import java.time.LocalDateTime

@Suppress("JpaAttributeTypeInspection")
@Entity(name = "Feedback")
@Table(name = "feedback")
data class FeedbackData(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long = 0,
    @Column var subject: String,
    @Column var description: String,
    @Column var topic: String,
    @Column var tags: TagData = TagData(),
    @Column var rating: Int,

    @Column var clientIp: String,
    @Column var userAgent: String,

    @Column var accountId: Long,

    @Column var created: LocalDateTime,
    @Column var updated: LocalDateTime? = null

) : DataObject {

    companion object {
        fun create(
            account: Account,
            request: FeedbackChangeRequest,
            clientIp: String,
            userAgent: String,
            timestamp: LocalDateTime
        ): FeedbackData {
            return FeedbackData(
                0,
                request.subject,
                request.description,
                request.topic,
                TagData.create(request.tags),
                request.rating,
                clientIp,
                userAgent,
                account.id,
                timestamp
            )
        }
    }


    fun convert(account: Account): Feedback {
        return Feedback(id, subject, description, topic, tags.tags, rating, account, clientIp, userAgent, updated ?: created)
    }


    fun update(request: FeedbackChangeRequest, timestamp: LocalDateTime): FeedbackData {
        subject = request.subject
        description = request.description
        topic = request.topic
        tags = TagData.create(request.tags)
        rating = request.rating
        updated = timestamp
        return this
    }
}
