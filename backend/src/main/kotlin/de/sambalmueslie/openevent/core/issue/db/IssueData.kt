package de.sambalmueslie.openevent.core.issue.db

import de.sambalmueslie.openevent.common.DataObject
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.issue.api.Issue
import de.sambalmueslie.openevent.core.issue.api.IssueChangeRequest
import de.sambalmueslie.openevent.core.issue.api.IssueStatus
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "Issue")
@Table(name = "issue")
data class IssueData(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long = 0,
    @Column var subject: String,
    @Column var description: String,

    @Column var error: String,
    @Column var url: String,

    @Column @Enumerated(EnumType.STRING) var status: IssueStatus,

    @Column var clientIp: String,
    @Column var userAgent: String,

    @Column var accountId: Long,

    @Column var created: LocalDateTime,
    @Column var updated: LocalDateTime? = null
) : DataObject {

    companion object {
        fun create(
            account: Account,
            request: IssueChangeRequest,
            clientIp: String,
            userAgent: String,
            timestamp: LocalDateTime
        ): IssueData {
            return IssueData(
                0,
                request.subject,
                request.description,
                request.error,
                request.url,
                IssueStatus.CREATED,
                clientIp,
                userAgent,
                account.id,
                timestamp
            )
        }
    }

    fun convert(account: Account): Issue {
        return Issue(id, subject, description, error, url, account, status, clientIp, userAgent, updated ?: created)
    }

    fun update(request: IssueChangeRequest, timestamp: LocalDateTime): IssueData {
        subject = request.subject
        description = request.description
        error = request.error
        url = request.url
        updated = timestamp
        return this
    }

    fun status(status: IssueStatus, timestamp: LocalDateTime): IssueData {
        this.status = status
        this.updated = timestamp
        return this
    }
}
